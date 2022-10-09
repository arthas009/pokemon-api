import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestClass {

    @Test(priority = 1)
    public void testPokemonNamesAreCorrect() {
        List<String> checkList = new ArrayList<>(Main.findNormalPokemons());
        Assert.assertEquals(checkList.get(0), "pidgeotto");
        Assert.assertEquals(checkList.get(1), "spearow");
        Assert.assertEquals(checkList.get(2), "fearow");
        Assert.assertEquals(checkList.get(3), "pidgeot");
        Assert.assertEquals(checkList.get(4), "raticate");
        Assert.assertEquals(checkList.get(5), "rattata");
        Assert.assertEquals(checkList.get(6), "pidgey");
    }
}
