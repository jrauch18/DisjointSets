/*
            Fall 2023
        COP 3503C Assignment 3
This program is written by: Jack Rauch */


import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        //read the number of nodes, total connections, and destroyed connections
        int n = stdin.nextInt();
        int m = stdin.nextInt();
        int d = stdin.nextInt();

        ArrayList<int[]> allConnections = new ArrayList<>();
        //track the existence of each connection
        boolean[] connectionStatus = new boolean[m];

        //read the connections and add them to the allConnections list
        for (int i = 0; i < m; i++) {
            int u = stdin.nextInt() - 1;
            int v = stdin.nextInt() - 1;
            allConnections.add(new int[]{u, v});
        }

        int[] destroyedConnections = new int[d];
        //mark the destroyed connections as not existing initially
        for (int i = 0; i < d; i++) {
            destroyedConnections[i] = stdin.nextInt() - 1;
            connectionStatus[destroyedConnections[i]] = true;
        }

        DisjointSet set = new DisjointSet(n);

        //initially perform unions for the connections that weren't destroyed
        for (int i = 0; i < m; i++) {
            if (!connectionStatus[i]) {
                set.union(allConnections.get(i)[0], allConnections.get(i)[1]);
            }
        }

        //store results for connectivity
        ArrayList<Long> results = new ArrayList<>();
        results.add(calculateConnectivity(set));

        //revert the destruction process
        for (int i = d - 1; i >= 0; i--) {
            if (connectionStatus[destroyedConnections[i]]) {
                set.union(allConnections.get(destroyedConnections[i])[0], allConnections.get(destroyedConnections[i])[1]);
                connectionStatus[destroyedConnections[i]] = false;
            }
            results.add(calculateConnectivity(set));
        }

        //print results in the reverse order
        for (int i = results.size() - 1; i >= 0; i--) {
            System.out.println(results.get(i));
        }

        stdin.close();
    }

    //calculate the total connectivity based on sizes of each disjoint set
    static long calculateConnectivity(DisjointSet set) {
        long connectivity = 0;
        for (int size : set.sizes) {
            connectivity += (long) size * size;
        }
        return connectivity;
    }
}

class DisjointSet {
    private int[] parent, rank;
    int[] sizes;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        sizes = new int[n];

        //initially, every node is its own parent (disconnected)
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            sizes[i] = 1;
        }
    }

    //find the root of a node
    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    //union two sets together
    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI == rootJ) {
            return false; //they are already in the same set
        }

        //merge the two sets based on their rank
        if (rank[rootI] < rank[rootJ]) {
            parent[rootI] = rootJ;
            sizes[rootJ] += sizes[rootI];
            sizes[rootI] = 0;
        } else if (rank[rootI] > rank[rootJ]) {
            parent[rootJ] = rootI;
            sizes[rootI] += sizes[rootJ];
            sizes[rootJ] = 0;
        } else {
            parent[rootJ] = rootI;
            sizes[rootI] += sizes[rootJ];
            sizes[rootJ] = 0;
            rank[rootI]++;
        }

        return true;
    }
}
