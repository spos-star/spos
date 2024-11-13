#include <bits/stdc++.h>
using namespace std;

int firstFit(vector<int>& blocks, int req) {
    for (int i = 0; i < blocks.size(); i++) {
        if (blocks[i] >= req) {
            blocks[i] -= req;
            return i;
        }
    }
    return -1;
}

int bestFit(vector<int>& blocks, int req) {
    int ind = -1, mn = INT_MAX;
    for (int i = 0; i < blocks.size(); i++) {
        if (blocks[i] >= req && blocks[i] - req < mn) {
            mn = blocks[i] - req;
            ind = i;
        }
    }
    if (ind != -1) blocks[ind] -= req;
    return ind;
}

int nextFit(vector<int>& blocks, int req, int lind) {
    int n = blocks.size();
    int i = lind;
    while (true) {
        if (blocks[i] >= req) {
            blocks[i] -= req;
            int ind = i;
            i = (i + 1) % n;  // Update last index
            return ind;
        }
        i = (i + 1) % n;
        if (i == lind) break;  // We've checked all blocks
    }
    return -1;
}

int worstFit(vector<int>& blocks, int req) {
    int ind = -1, mx = INT_MIN;
    for (int i = 0; i < blocks.size(); i++) {
        if (blocks[i] >= req && blocks[i] - req > mx) {
            mx = blocks[i] - req;
            ind = i;
        }
    }
    if (ind != -1) blocks[ind] -= req;
    return ind;
}

int main() {
    vector<int> blocks = {100, 500, 200, 300, 600};
    vector<int> req = {212, 417, 112, 426};

    cout << "\n--- First Fit ---\n";
    vector<int> blocksCopy = blocks;
    for (int r : req) {
        int result = firstFit(blocksCopy, r);
        cout << "Process " << r << " -> Block " << (result != -1 ? to_string(result) : "Not Allocated") << endl;
    }

    cout << "\n--- Best Fit ---\n";
    blocksCopy = blocks;
    for (int r : req) {
        int result = bestFit(blocksCopy, r);
        cout << "Process " << r << " -> Block " << (result != -1 ? to_string(result) : "Not Allocated") << endl;
    }

    cout << "\n--- Next Fit ---\n";
    blocksCopy = blocks;
    int lastIndex = 0;
    for (int r : req) {
        int result = nextFit(blocksCopy, r, lastIndex);
        cout << "Process " << r << " -> Block " << (result != -1 ? to_string(result) : "Not Allocated") << endl;
    }

    cout << "\n--- Worst Fit ---\n";
    blocksCopy = blocks;
    for (int r : req) {
        int result = worstFit(blocksCopy, r);
        cout << "Process " << r << " -> Block " << (result != -1 ? to_string(result) : "Not Allocated") << endl;
    }

    return 0;
}