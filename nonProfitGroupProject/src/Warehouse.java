
public class Warehouse implements Comparable<Warehouse> {

    private int id;
    private String phoneNum;
    private int storageCapacity;
    private String city;
    private String address;
    private String managerName;
    private int droneCapacity;

    public Warehouse(int id, String phoneNum, int storageCapacity, String city,
            String address, String managerName, int droneCapacity) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.storageCapacity = storageCapacity;
        this.city = city;
        this.address = address;
        this.managerName = managerName;
        this.droneCapacity = droneCapacity;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getStorageCapacity() {
        return this.storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getDroneCapacity() {
        return this.droneCapacity;
    }

    public void setDroneCapacity(int droneCapacity) {
        this.droneCapacity = droneCapacity;
    }

    @Override
    public int compareTo(Warehouse w) {
        if (this.id == w.getId()) {
            return 0;
        } else if (this.id > w.getId()) {
            return 1;
        } else {
            return -1;
        }
    }
}