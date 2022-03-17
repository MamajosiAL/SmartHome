package be.ucll.java.mobile.smarthome_mobile.util;

public class HouseSpinner {
        public int ID;
        public String name;

        public HouseSpinner(int ID, String name){
                this.ID =ID;
                this.name = name;
        }

        @Override
        public String toString() {
                return this.name; // What to display in the Spinner list.
        }

}
