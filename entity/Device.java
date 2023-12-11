public class Device {
    /** 设备编号 */
    private String id;
    /** 设备名称 */
    private String name;
    /** 领用人 */
    private String who;
    /** 设备类型 */
    private String type;
    /** 设备状态（是否借出）*/
    private boolean isBorrowed;
    /** 是否是报废设备 */
    private boolean isDeprecated;

    public Device(String id, String name, String who, String type, boolean isBorrowed, boolean isDeprecated) {
        this.id = id;
        this.name = name;
        this.who = who;
        this.type = type;
        this.isBorrowed = isBorrowed;
        this.isDeprecated = isDeprecated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
    }

    
}
