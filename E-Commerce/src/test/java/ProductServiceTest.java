import application.entity.Product;
import application.entity.Type;
import application.repository.ProductRepository;
import application.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void before() {
        productService = new ProductService(productRepository);

        Type type1 = new Type(new Long(1), "lipgloss");
        Type type2 = new Type(new Long(2), "lipstain");
        Type type3 = new Type(new Long(3), "blush");
        Type type4 = new Type(new Long(4), "eyeshadow");

        Product product1 = new Product();
        product1.setId(new Long(1));
        product1.setName("Lipstain Berlin");
        product1.setCompany("Sleek");
        product1.setType(type2);
        product1.setQuantity(new Integer(200));
        product1.setPrice(new Double(17.99));

        Product product2 = new Product();
        product2.setId(new Long(2));
        product2.setName("Blush double bomb");
        product2.setCompany("Sephora");
        product2.setType(type3);
        product2.setQuantity(new Integer(110));
        product2.setPrice(new Double(23.99));

        Product product3 = new Product();
        product3.setId(new Long(3));
        product3.setName("Eyeshadow Massive Dark");
        product3.setCompany("Dior");
        product3.setType(type4);
        product3.setQuantity(new Integer(200));
        product3.setPrice(new Double(47.99));

        Product product4 = new Product();
        product4.setId(new Long(4));
        product4.setName("Glitter Attack");
        product4.setCompany("Kiko");
        product4.setType(type1);
        product4.setQuantity(new Integer(200));
        product4.setPrice(new Double(17.99));


        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        Mockito.when(productRepository.findAll()).thenReturn(products);
        Optional<Product> optional = Optional.of(product2);
        Mockito.when(productRepository.findById(new Long(2))).thenReturn(optional);
        Mockito.when(productRepository.findByName(new String("Lipstain Berlin"))).thenReturn(product1);
        Mockito.when(productRepository.findAllByCompany(new String("Sleek"))).thenReturn(products);
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product4);
    }

    @Test
    public void testFindAll() {
        Iterable<Product> products = productService.findAll();
        ArrayList<Product> productsArrayList = new ArrayList<>();
        products.forEach(productsArrayList::add);
        Assert.assertEquals(3, productsArrayList.size());
    }

    @Test
    public void testFindById() {
        Product product = productService.findById(new Long(2));
        Assert.assertEquals(new Long(2), product.getId());
    }

    @Test
    public void testFindByName() {
        Product product = productService.findByName(new String("Lipstain Berlin"));
        Assert.assertEquals(new Long(1), product.getId());
    }

    @Test
    public void testFindAllByCompany() {
        Iterable<Product> products = productService.findAllByCompany("Sleek");
        ArrayList<Product> productsArrayList = new ArrayList<>();
        products.forEach(productsArrayList::add);
        Assert.assertEquals(new String("Sleek"), productsArrayList.get(0).getName());
    }

    @Test
    public void create() {
        Product product = productService.create(new Product());
        Assert.assertEquals(new Long(4), product.getId());
    }

    @Test
    public void update() {
        Product product = productService.create(new Product());
        product.setName("Updated name");
        Assert.assertEquals(new String("Updated name"), product.getName());
    }
}