package View;

import Model.Level;
import Model.User;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Program {
    public List<Level> levelList = new List<Level>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NotNull
        @Override
        public Iterator<Level> iterator() {
            return null;
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Level level) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends Level> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NotNull Collection<? extends Level> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public Level get(int index) {
            return null;
        }

        @Override
        public Level set(int index, Level element) {
            return null;
        }

        @Override
        public void add(int index, Level element) {

        }

        @Override
        public Level remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NotNull
        @Override
        public ListIterator<Level> listIterator() {
            return null;
        }

        @NotNull
        @Override
        public ListIterator<Level> listIterator(int index) {
            return null;
        }

        @NotNull
        @Override
        public List<Level> subList(int fromIndex, int toIndex) {
            return List.of();
        }
    };

    public void initializeLevels() {
        Level ant = new Level ("Worker Ant", 0, 5000, "ant.png");
        Level worm = new Level ("Book Worm", 5001, 10000, "worm.png");
        Level beetle = new Level ("Busy Beetle", 10001, 15000, "ladybug.png");
        Level bee = new Level ("Spelling Bee", 15001, 20000, "bee.png");
        Level butterfly = new Level ("Study Butterfly", 20001, 25000, "worm.png");
        levelList.add(ant);
        levelList.add(worm);
        levelList.add(beetle);
        levelList.add(bee);
        levelList.add(butterfly);
    }

    public void signIn() {
        User.getLogin();
    }

    public void signUp() {
        String type = User.getUserType();
        if (type.equals("student")) {
            User.CreateNewStudent();
        }
        else {
            User.CreateNewTeacher();
        }
    }
}
