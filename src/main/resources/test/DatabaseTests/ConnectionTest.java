package DatabaseTests;

import Model.DatabaseConnectionFailedException;
import Model.MySQL;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {
    @Test
    public void establish_conection() {
        assertDoesNotThrow(() -> MySQL.establishConnection());
    }
}
