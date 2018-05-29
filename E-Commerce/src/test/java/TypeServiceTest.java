import application.entity.Type;
import application.repository.TypeRepository;
import application.service.TypeService;
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
public class TypeServiceTest {
    private TypeService typeService;

    @Mock
    private TypeRepository typeRepository;

    @Before
    public void before() {
        typeService = new TypeService(typeRepository);

        Type type1 = new Type();
        type1.setId(new Long(1));
        type1.setType("lipstain");

        Type type2 = new Type();
        type2.setId(new Long(2));
        type2.setType("lipgloss");

        Type type3 = new Type();
        type3.setId(new Long(3));
        type3.setType("blush");

        Type type4 = new Type();
        type4.setId(new Long(4));
        type4.setType("eyeshadow");

        ArrayList<Type> types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
        types.add(type3);

        Mockito.when(typeRepository.findAll()).thenReturn(types);
        Optional<Type> optional = Optional.of(type2);
        Mockito.when(typeRepository.findById(new Long(2))).thenReturn(optional);
        Mockito.when(typeRepository.findByType("lipstain")).thenReturn(type1);
        Mockito.when(typeRepository.save(Mockito.any())).thenReturn(type4);
    }

    @Test
    public void testFindAll() {
        Iterable<Type> types = typeService.findAll();
        ArrayList<Type> typeArrayList = new ArrayList<>();
        types.forEach(typeArrayList::add);
        Assert.assertEquals(3, typeArrayList.size());
    }

    @Test
    public void testFindById() {
        Type type = typeService.findById(new Long(2));
        Assert.assertEquals(new Long(2), type.getId());
    }

    @Test
    public void testFindByName() {
        Type type = typeService.findByName(new String("lipstain"));
        Assert.assertEquals(new String("lipstain"), type.getType());
    }
}

