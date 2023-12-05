
public class Equipment implements Comparable<Equipment> {

    private int inventoryId;
    private String size;
    private String manufacturer;
    private String weight;
    private String warrantyExp;
    private String arrivalDate;
    private String description;
    private int year;
    private String equipmentType;
    private int modelNum;
    private int serialNum;
    private Boolean delivered;

    public Equipment(int InventoryId, String size, String manufacturer,
            String weight, String warrantyExp, String arrivalDate,
            String description, int year, String equipmentType, int modelNum,
            int serialNum, Boolean delivered) {
        this.inventoryId = InventoryId;
        this.size = size;
        this.manufacturer = manufacturer;
        this.weight = weight;
        this.warrantyExp = warrantyExp;
        this.arrivalDate = arrivalDate;
        this.description = description;
        this.year = year;
        this.equipmentType = equipmentType;
        this.modelNum = modelNum;
        this.serialNum = serialNum;
        this.delivered = delivered;
    }

    public Boolean getDelivered() {
        return this.delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWarrantyExp() {
        return this.warrantyExp;
    }

    public void setWarrantyExp(String warrantyExp) {
        this.warrantyExp = warrantyExp;
    }

    public String getArrivalDate() {
        return this.arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEquipmentType() {
        return this.equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getModelNum() {
        return this.modelNum;
    }

    public void setModelNum(int modelNum) {
        this.modelNum = modelNum;
    }

    public int getSerialNum() {
        return this.serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public int compareTo(Equipment e) {
        if (this.inventoryId == e.getInventoryId()) {
            return 0;
        } else if (this.inventoryId > e.getInventoryId()) {
            return 1;
        } else {
            return -1;
        }
    }
}
