#include<bits/stdc++.h>
using namespace std;

struct Process {
    int id;      // Process ID
    int at;      // Arrival Time
    int bt;      // Burst Time
    int ct;      // Completion Time
    int wt;      // Waiting Time
    int tat;     // Turnaround Time
    int priority; // Priority (lower number means higher priority)
    bool comp;   // Completion status
};

bool arrivalSort(Process a, Process b) {
    if (a.at == b.at)
        return a.priority < b.priority; // Sort by priority if arrival times are the same
    return a.at < b.at; // Otherwise, sort by arrival time
}

void priorityScheduling(vector<Process>& p) {
    int n = p.size();
    int ctm = 0; // Current time
    int cnt = 0; // Count of completed processes

    while (cnt != n) {
        int ind = -1;
        int highestPriority = INT_MAX;

        for (int i = 0; i < n; i++) {
            if (!p[i].comp && p[i].at <= ctm && p[i].priority < highestPriority) {
                ind = i;
                highestPriority = p[i].priority;
            }
        }

        if (ind == -1) {
            ctm++; 
        } else {
           
            p[ind].ct = ctm + p[ind].bt; // Completion time
            p[ind].tat = p[ind].ct - p[ind].at; // Turnaround time
            p[ind].wt = p[ind].tat - p[ind].bt; // Waiting time
            p[ind].comp = true; // Mark process as completed

            ctm = p[ind].ct; // Move current time to the completion time of this process
            cnt++; // Increase count of completed processes
        }
    }
}

int main() {
    int n;
    cout << "Enter the number of processes: ";
    cin >> n;
    vector<Process> p(n);

    cout << "Enter process ID, Arrival Time, Burst Time, and Priority for each process:\n";
    for (int i = 0; i < n; i++) {
        cin >> p[i].id >> p[i].at >> p[i].bt >> p[i].priority;
        p[i].comp = false; // Initially, no process is completed
    }

    // Sort processes by arrival time, and by priority if arrival times are equal
    sort(p.begin(), p.end(), arrivalSort);

    // Apply Priority Scheduling
    priorityScheduling(p);

    // Display the results
    for(int i=0;i<n;i++){
        cout<<p[i].ct<<" "<<p[i].wt<<" "<<p[i].tat<<endl;
    }

    return 0;
}
