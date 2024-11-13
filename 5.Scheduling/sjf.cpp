#include<bits/stdc++.h>
using namespace std;

struct Process{
    int id;
    int at;
    int bt;
    int rt;
    int ct;
    int wt;
    int tat;
    bool comp;
};

bool comp(Process a, Process b){
    return a.at < b.at; // Sort by arrival time initially
}

void premptiveSJF(vector<Process>& p){
    int n = p.size();
    int cnt = 0;
    int ctm = 0;
    
    while(cnt != n){
        int ind = -1, mn = INT_MAX;
        for(int i = 0; i < n; i++){
            if(!p[i].comp && p[i].at <= ctm && p[i].rt < mn){
                ind = i;
                mn = p[i].rt;
            }
        }
        
        if(ind == -1) {
            ctm++;
        }
        else {
            p[ind].rt--;
            if(p[ind].rt == 0){
                p[ind].comp = true;
                p[ind].ct = ctm + 1; // Completion time is current time + 1
                p[ind].wt = p[ind].ct - p[ind].at - p[ind].bt;
                p[ind].tat = p[ind].wt + p[ind].bt;
                cnt++;
            }
            ctm++;
        }
    }
}

int main(){
    int n;
    cin >> n;
    vector<Process> p(n);
    
    for(int i = 0; i < n; i++){
        cin >> p[i].id >> p[i].at >> p[i].bt;
        p[i].rt = p[i].bt;
        p[i].comp = false;
    }
    
    sort(p.begin(), p.end(), comp); // Sort by arrival time initially
    premptiveSJF(p);
    
    // Sort by process ID before output
    sort(p.begin(), p.end(), [](Process a, Process b) {
        return a.id < b.id;
    });
    
    for(int i = 0; i < n; i++){
        cout << "Process ID: " << p[i].id << " Completion Time: " << p[i].ct 
             << " Waiting Time: " << p[i].wt << " Turnaround Time: " << p[i].tat << endl;
    }
}
