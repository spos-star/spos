#include<bits/stdc++.h>

using namespace std;

// FIFO Page Replacement Algorithm
int fifoPageReplacement(vector<int>& pages, int frames) {
    queue<int> q;
    unordered_set<int> s;
    int pageFaults = 0;

    for (int page : pages) {
        if (s.size() < frames) {
            if (s.find(page) == s.end()) {
                s.insert(page);
                q.push(page);
                pageFaults++;
            }
        } else {
            if (s.find(page) == s.end()) {
                int removedPage = q.front();
                q.pop();
                s.erase(removedPage);
                s.insert(page);
                q.push(page);
                pageFaults++;
            }
        }
    }
    return pageFaults;
}

// LRU Page Replacement Algorithm
int lruPageReplacement(vector<int>& pages, int frames) {
    unordered_map<int, int> pageMap;
    int pageFaults = 0;

    for (int i = 0; i < pages.size(); i++) {
        int page = pages[i];

        if (pageMap.size() < frames) {
            if (pageMap.find(page) == pageMap.end()) {
                pageFaults++;
            }
            pageMap[page] = i;
        } else {
            if (pageMap.find(page) == pageMap.end()) {
                int lruPage = -1, lruIndex = i;
                for (auto it : pageMap) {
                    if (it.second < lruIndex) {
                        lruIndex = it.second;
                        lruPage = it.first;
                    }
                }
                pageMap.erase(lruPage);
                pageMap[page] = i;
                pageFaults++;
            } else {
                pageMap[page] = i;
            }
        }
    }
    return pageFaults;
}

// Optimal Page Replacement Algorithm
int optimalPageReplacement(vector<int>& pages, int frames) {
    unordered_set<int> s;
    int pageFaults = 0;

    for (int i = 0; i < pages.size(); i++) {
        int page = pages[i];

        if (s.size() < frames) {
            if (s.find(page) == s.end()) {
                s.insert(page);
                pageFaults++;
            }
        } else {
            if (s.find(page) == s.end()) {
                int farthest = i, victimPage = -1;
                for (int pg : s) {
                    int nextUse = find(pages.begin() + i + 1, pages.end(), pg) - pages.begin();
                    if (nextUse > farthest) {
                        farthest = nextUse;
                        victimPage = pg;
                    }
                }
                s.erase(victimPage);
                s.insert(page);
                pageFaults++;
            }
        }
    }
    return pageFaults;
}

int main() {
    vector<int> pages = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
    int frames = 3;

    cout << "FIFO Page Faults: " << fifoPageReplacement(pages, frames) << endl;
    cout << "LRU Page Faults: " << lruPageReplacement(pages, frames) << endl;
    cout << "Optimal Page Faults: " << optimalPageReplacement(pages, frames) << endl;

    return 0;
}