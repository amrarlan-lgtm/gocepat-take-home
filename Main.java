interface Kendaraan {
    double hitungTarif(double jarakKm);
    String getJenis();
}

class Motor implements Kendaraan {
    private static final double TARIF_DASAR = 5_000, TARIF_PER_KM = 2_500;
    public double hitungTarif(double jarakKm) { return TARIF_DASAR + TARIF_PER_KM * jarakKm; }
    public String getJenis() { return "Motor"; }
}

class Mobil implements Kendaraan {
    private static final double TARIF_DASAR = 8_000, TARIF_PER_KM = 4_000;
    public double hitungTarif(double jarakKm) { return TARIF_DASAR + TARIF_PER_KM * jarakKm; }
    public String getJenis() { return "Mobil"; }
}

class MobilListrik implements Kendaraan {
    private static final double TARIF_DASAR = 12_000, TARIF_PER_KM = 3_000;
    public double hitungTarif(double jarakKm) { return TARIF_DASAR + TARIF_PER_KM * jarakKm; }
    public String getJenis() { return "Mobil Listrik"; }
}

// Kendaraan baru: tidak ada kode lain yang perlu diubah
class SepedaListrik implements Kendaraan {
    public double hitungTarif(double km) { return 3_000 + 1_800 * km; }
    public String getJenis() { return "Sepeda Listrik"; }
}

class Driver {
    private final String nama;
    Driver(String nama) { this.nama = nama; }
    String getNama() { return nama; }
}

class Penumpang {
    private final String nama;
    Penumpang(String nama) { this.nama = nama; }
    String getNama() { return nama; }
}

// Aggregation: Driver, Penumpang, Kendaraan dibuat di luar lalu "dipinjam" Trip
class Trip {
    private final Driver driver;
    private final Penumpang penumpang;
    private final Kendaraan kendaraan;
    private final double jarakKm;

    Trip(Driver driver, Penumpang penumpang, Kendaraan kendaraan, double jarakKm) {
        this.driver = driver;
        this.penumpang = penumpang;
        this.kendaraan = kendaraan;
        this.jarakKm = jarakKm;
    }

    double hitungBiaya() { return kendaraan.hitungTarif(jarakKm); }

    void cetakRingkasan() {
        System.out.println("Trip: " + penumpang.getNama() + " dengan driver " + driver.getNama()
                + " (" + kendaraan.getJenis() + ", " + jarakKm + " km) = Rp"
                + String.format("%.0f", hitungBiaya()));
    }
}

public class Main {
    public static void main(String[] args) {
        // 1a. Polymorphism
        System.out.println("=== Tarif 10 km ===");
        Kendaraan[] daftar = { new Motor(), new Mobil(), new MobilListrik() };
        for (Kendaraan k : daftar) {
            System.out.println(k.getJenis() + ": Rp" + String.format("%.0f", k.hitungTarif(10)));
        }

        // 1b. Trip (aggregation): driver tetap hidup setelah trip selesai
        System.out.println("\n=== Trip ===");
        Driver budi = new Driver("Budi");
        Penumpang sari = new Penumpang("Sari");
        Trip trip1 = new Trip(budi, sari, new MobilListrik(), 8);
        trip1.cetakRingkasan();

        Trip trip2 = new Trip(budi, new Penumpang("Andi"), new Motor(), 3); // Budi dipakai lagi
        trip2.cetakRingkasan();

        // 1c. Jenis baru tanpa mengubah Trip
        System.out.println("\n=== Kendaraan baru ===");
        Trip trip3 = new Trip(new Driver("Dewi"), sari, new SepedaListrik(), 5);
        trip3.cetakRingkasan();
    }
}
