#include<bits/stdc++.h>
using namespace std;

struct Process {
    int id;     // Process ID
    int at;     // Arrival Time
    int bt;     // Burst Time
    int rt;     // Remaining Time
    int ct;     // Completion Time
    int wt;     // Waiting Time
    int tat;    // Turnaround Time
};

void roundRobin(vector<Process>& p, int quantum) {
    int n = p.size();
    int ctm = 0; // Current time
    int cnt = 0; // Count of completed processes
    queue<int> q; // Queue to hold the indexes of processes
    vector<bool> inQueue(n, false); // Track if a process is in the queue

    // Sort by arrival time
    sort(p.begin(), p.end(), [](Process a, Process b) {
        return a.at < b.at;
    });

    // Push the first process into the queue
    q.push(0);
    inQueue[0] = true;

    while (cnt != n) {
        if (q.empty()) {
            // If no processes are in the queue, increment time until a process arrives
            for (int i = 0; i < n; i++) {
                if (!inQueue[i] && p[i].at <= ctm) {
                    q.push(i);
                    inQueue[i] = true;
                    break;
                }
            }
            ctm++;
            continue;
        }

        int i = q.front();
        q.pop();
        inQueue[i] = false;

        // Execute the process for a time slice (quantum)
        if (p[i].rt > quantum) {
            ctm += quantum;
            p[i].rt -= quantum;
        }
        else {
            ctm += p[i].rt;
            p[i].rt = 0;

            // Process is completed
            p[i].ct = ctm;
            p[i].tat = p[i].ct - p[i].at;
            p[i].wt = p[i].tat - p[i].bt;
            cnt++;
        }

        // Add newly arrived processes to the queue
        for (int j = 0; j < n; j++) {
            if (!inQueue[j] && p[j].at <= ctm && p[j].rt > 0) {
                q.push(j);
                inQueue[j] = true;
            }
        }

        // If the process is not finished, put it back into the queue
        if (p[i].rt > 0) {
            q.push(i);
            inQueue[i] = true;
        }
    }
}




int main() {
    int n, quantum;
    cout << "Enter the number of processes: ";
    cin >> n;
    cout << "Enter the time quantum: ";
    cin >> quantum;

    vector<Process> p(n);
    cout << "Enter process ID, Arrival Time, and Burst Time for each process:\n";
    for (int i = 0; i < n; i++) {
        cin >> p[i].id >> p[i].at >> p[i].bt;
        p[i].rt = p[i].bt; // Initialize remaining time with burst time
    }

    // Apply Round Robin Scheduling
    roundRobin(p, quantum);

    // Display the results
    cout << "\nProcess ID\tArrival Time\tBurst Time\tCompletion Time\tWaiting Time\tTurnaround Time\n";
    for (int i = 0; i < n; i++) {
        cout << p[i].id << "\t\t" << p[i].at << "\t\t" << p[i].bt << "\t\t" << p[i].ct
             << "\t\t" << p[i].wt << "\t\t" << p[i].tat << endl;
    }

    return 0;
}
