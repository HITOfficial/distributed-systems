public enum ServiceType {
    PASSER_TRANSPORT("passer_transport"),
    CARGO_TRANSPORT("cargo_transport"),
    SATELLITE_DEPLOYED("satellite_deployed");

    private final String name;
    ServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
