import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
        CardTest.class,
        DeckTest.class,
        PlayerTest.class,
        CardGameTest.class,
        RoundRobinTest.class
})
public class TestSuiteTest {

}