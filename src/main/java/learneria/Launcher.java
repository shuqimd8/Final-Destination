package learneria;

import com.example.finaldestinationgroupproject.MySQL;
import com.example.finaldestinationgroupproject.User;

public class Launcher {

    
    public static void main(String[] args) {
        MySQL.establishConnection();

        User.getStudents();
        User.getTeachers();
    }
}
