#include<bits/stdc++.h>
using namespace std;
struct Process{
    int id;
    int at;
    int bt;
    int wt;
    int tat;
};
bool comp(Process a,Process b){
    return a.id<b.id;
}
float avgWT(vector<Process>&p){
    int n=p.size();
    p[0].wt=0;
    float val=0;
    for(int i=1;i<n;i++){
        p[i].wt=p[i-1].at+p[i-1].bt+p[i-1].wt-p[i].at;
        if(p[i].wt<0) p[i].wt=0;
        val+=p[i].wt;
    }
    return val/float(n);
}
float avgTAT(vector<Process>&p){
    int n=p.size();
    p[0].tat=p[0].bt;
    float val=0;
    for(int i=1;i<n;i++){
        p[i].tat=p[i].wt+p[i].bt;
        val+=p[i].tat;
    }
    return val/float(n);
}
int main(){
    int n;cin>>n;
    vector<Process>p(n);
    for(int i=0;i<n;i++){
        cin>>p[i].id>>p[i].at>>p[i].bt;
        p[i].wt=0;
        p[i].tat=0;
    }
    sort(p.begin(),p.end(),comp);
    cout<<"Average Waiting Time: "<<avgWT(p)<<endl;
    cout<<"Average TAT: "<<avgTAT(p)<<endl;
    // for(int i=0;i<n;i++){
    //     cout<<p[i].wt<<" "<<p[i].tat<<endl;
    // }

}