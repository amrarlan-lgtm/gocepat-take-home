public class GoCepat {

    static class GrafPeta {
        private final int n;
        private final int[][] adj;   // adj[u] = daftar tetangga (growable)
        private final int[] deg;     // jumlah tetangga terisi
        int hopTerakhir = -1;        // jumlah hop hasil pencarian terakhir

        GrafPeta(int n) {
            this.n = n;
            adj = new int[n][];
            deg = new int[n];
            for (int i = 0; i < n; i++) adj[i] = new int[2]; // kapasitas awal 2
        }

        // jalan dua arah
        void tambahSisi(int u, int v) {
            tambahTetangga(u, v);
            tambahTetangga(v, u);
        }

        private void tambahTetangga(int u, int v) {
            if (deg[u] == adj[u].length) {            // penuh -> perbesar 2x
                int[] baru = new int[adj[u].length * 2];
                for (int i = 0; i < deg[u]; i++) baru[i] = adj[u][i];
                adj[u] = baru;
            }
            adj[u][deg[u]++] = v;
        }

        /** Mengembalikan simpul driver terdekat (dalam hop) dari 'asal', atau -1 jika tidak ada. */
        int bfsCariTerdekat(int asal, boolean[] adalahDriver) {
            hopTerakhir = -1;
            if (adalahDriver[asal]) { hopTerakhir = 0; return asal; }

            boolean[] visited = new boolean[n];   // status dikunjungi
            int[] jarak = new int[n];             // jumlah hop dari asal
            int[] queue = new int[n];             // queue array biasa (tiap simpul masuk maks. 1x)
            int head = 0, tail = 0;

            queue[tail++] = asal;
            visited[asal] = true;

            while (head < tail) {
                int u = queue[head++];            // dequeue
                for (int i = 0; i < deg[u]; i++) {
                    int v = adj[u][i];
                    if (!visited[v]) {
                        visited[v] = true;
                        jarak[v] = jarak[u] + 1;
                        if (adalahDriver[v]) {    // driver pertama ditemukan = terdekat
                            hopTerakhir = jarak[v];
                            return v;
                        }
                        queue[tail++] = v;        // enqueue
                    }
                }
            }
            return -1; // tidak ada driver yang terjangkau
        }
    }

    public static void main(String[] args) {
        // 10 persimpangan (0..9)
        GrafPeta peta = new GrafPeta(10);
        int[][] jalan = {
            {0,1},{0,2},{1,3},{2,3},{3,4},
            {4,5},{4,6},{5,7},{6,7},{7,8},{8,9}
        };
        for (int[] j : jalan) peta.tambahSisi(j[0], j[1]);

        // driver tersebar di persimpangan 4 dan 9
        boolean[] adalahDriver = new boolean[10];
        adalahDriver[4] = true;
        adalahDriver[9] = true;

        int[] penumpang = {0, 8};
        for (int p : penumpang) {
            int hasil = peta.bfsCariTerdekat(p, adalahDriver);
            if (hasil == -1) {
                System.out.println("Penumpang di simpul " + p + ": tidak ada driver terjangkau");
            } else {
                System.out.println("Penumpang di simpul " + p + " -> driver terdekat di simpul "
                        + hasil + " (" + peta.hopTerakhir + " hop)");
            }
        }
    }
}
