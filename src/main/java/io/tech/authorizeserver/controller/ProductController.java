package io.tech.authorizeserver.controller;

import io.tech.authorizeserver.entity.Product;
import io.tech.authorizeserver.model.Message;
import io.tech.authorizeserver.model.ProductDto;
import io.tech.authorizeserver.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<List<Product>> list(){
        List<Product> list = productService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") int id){
        if(!productService.existsById(id))
            return new ResponseEntity(new Message("no exist"), HttpStatus.NOT_FOUND);
        Product product = productService.getOne(id).get();
        return new ResponseEntity(product, HttpStatus.OK);
    }

    @GetMapping("/detailname/{name}")
    public ResponseEntity<Product> getByNombre(@PathVariable("name") String name){
        if(!productService.existsByName(name))
            return new ResponseEntity(new Message("no exist"), HttpStatus.NOT_FOUND);
        Product product = productService.getByName(name).get();
        return new ResponseEntity(product, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDto productDto){
        if(StringUtils.isBlank(productDto.getName()))
            return new ResponseEntity(new Message("name is required"), HttpStatus.BAD_REQUEST);
        if(productDto.getPrice()==null || productDto.getPrice()<0 )
            return new ResponseEntity(new Message("the price should be greater than 0"), HttpStatus.BAD_REQUEST);
        if(productService.existsByName(productDto.getName()))
            return new ResponseEntity(new Message("that name already exists"), HttpStatus.BAD_REQUEST);
        Product product = new Product(productDto.getName(), productDto.getPrice());
        productService.save(product);
        return new ResponseEntity(new Message("product created"), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id, @RequestBody ProductDto productDto){
        if(!productService.existsById(id))
            return new ResponseEntity(new Message("no exist"), HttpStatus.NOT_FOUND);
        if(productService.existsByName(productDto.getName()) && productService.getByName(productDto.getName()).get().getId() != id)
            return new ResponseEntity(new Message("that name already exists"), HttpStatus.BAD_REQUEST);
        if(StringUtils.isBlank(productDto.getName()))
            return new ResponseEntity(new Message("name is required"), HttpStatus.BAD_REQUEST);
        if(productDto.getPrice()==null || productDto.getPrice()<0 )
            return new ResponseEntity(new Message("the price should be greater than 0"), HttpStatus.BAD_REQUEST);

        Product product = productService.getOne(id).get();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productService.save(product);
        return new ResponseEntity(new Message("product updated"), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if(!productService.existsById(id))
            return new ResponseEntity(new Message("no exist"), HttpStatus.NOT_FOUND);
        productService.delete(id);
        return new ResponseEntity(new Message("product created"), HttpStatus.OK);
    }
}
