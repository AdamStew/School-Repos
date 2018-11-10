#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <iostream>
#include <map>
#include <vector>

using namespace std;

/**********************************************************
* Authors: Adam Stewart              		  *
* Assignment: Project 2 - Server Control System    		  *
* Due Date: 10/05/2017                             		  *
*      											   		  *
* Descripton: Simulates being able to create servers 	  *
* (eg. Web Server) with a minimum and maximum number 	  *
* of processes that can be created.  Additional processes *
* can be added, as long as it doesn't surpass the maximum *
* for that given server.  Additional servers can also be  *
* created.  Servers and processes can also be terminated  * 
* (as long as processes doesn't fall below minimum).  If  *
* a process terminates abnormally, it'll restart 		  *
* automatically.  			   							  *
**********************************************************/

void sendPID(); //Function to send a process' PID to their parent.
void sigExit(int); //Customized shutdown signal.
void sigAbnorm(int); //Customized abnormal shutdown signal.
void sigIncrease(int); //User defined signal 1, for incrementing processes.
void sigDecrease(int); //User defined signal 2, for decrementing processes.

vector<pid_t> childProcs; //Keep track of children for a respective server.
int generation = 0; //Parent = 0; Server = 1; Server Process = 2;
int op[2] = {0, 0}; //01 = Decrement; 10 = Increment; 11 = Reboot;
int fd[2]; //Read and write ports for piping.

/*************************************************************
* SERVER STRUCT:										     *
* 	minProcs - Integer to keep track of minimum processes.   *
* 	maxProcs - Integer to keep track of maximum processes. 	 *
*	numActive - Integer to keep track of the number of 		 *
*	currently active processes.								 *
*	pid - Pid_t to keep track of each process' PID value. 	 *
*************************************************************/
struct server {
	int minProcs;
	int maxProcs;
	int numActive;
	pid_t pid;
};

int main() {

	signal(SIGCHLD, SIG_IGN); //SIG_IGN causes children to terminate immediate, preventing zombies.
	signal(SIGINT, sigExit); //Every process the custom signal interrupt.
	
	std::map<std::string, server> hash; //Keep track of each server.
	pid_t pid;

	cout << "Welcome.  Generate servers with \"createServer min max serverName\", delete servers with \"abortServer serverName\", ";
	cout <<	"generate new processes with \"createProcess serverName\", delete processes with \"abortProcesses serverName\", and ";
	cout << "display a tree structure with \"displayStatus\".\n\n";

	while(1) {
		char *input = new char[1024]; 
		fgets(input, 1024, stdin); //Get user instruction.
		char *cmd = strtok(input, " ");
		

		if(strcmp(cmd, "createServer") == 0) { //If we're creating a server, check basic rules, and then create server with
			int min = atoi(strtok(NULL, " ")); //default number of processes.
			int max = atoi(strtok(NULL, " "));
			char *servName = strtok(NULL, " ");
			servName[strcspn(servName, "\n")] = '\0'; //Gets rid of \n char.

			if(min < 0 || max < min || hash.count(std::string(servName))) {
				cout << "Enter valid input.\n"; //Need valid numbers and name.
			} else {
				cout << "Created server " << servName << " with " << min << " children to start. \n";
				if((pid = fork()) == 0) { //Child goes here.
					generation++;
					signal(SIGUSR1, sigIncrease);
					signal(SIGUSR2, sigDecrease);
					pipe(fd); //Pipe for server-process communication.

					//By default, creates 'min' processes.
					for(int i=0; i < min; i++) { 
						if((pid = fork()) == 0) { //Grandchild goes here.
							generation++;
							signal(SIGABRT, sigAbnorm);
							while(1) {
								//Processes do nothing.
								pause();
							}
						} else {
							childProcs.push_back(pid); //Store grandchild PIDs.
						}
					}
					while(1) {
						//Child sits here, for now.
						pause();

						if(op[0] && op[1]) { //Goes here when abnormal shutdown happens.
							cout << "Abnormal shutdown detected.  Process has been reset.\n";
							char buffer[8];
							ssize_t size = read(fd[0], buffer, 8); //Pipe PID into a buffer.
							if ((size > 0) && (size < (int)sizeof(buffer))) {
								buffer[size]='\0';
							}
							long tmp = strtol(buffer, (char **)NULL, 10); //CString to long.
							pid_t abnormPID = (pid_t)tmp; //Long to pid_t.
							for(int i=0; i < (int)childProcs.size(); i++) { //Check all children for crashed process PID.
								if(childProcs[i] == abnormPID) { //When we find a match, remove it, and create a new one in place.
									childProcs.erase(childProcs.begin() + i);
									if((pid = fork()) == 0) {
										generation++;
										signal(SIGABRT, sigAbnorm);
										while(1) {
											//Processes do nothing.
											pause();
										}
									}
									childProcs.push_back(pid); //Store grandchild PID.
									break; //Once we found a match, quit looking.
								}
							}
						} else if(op[0]) { //Goes here to process++.
							if((pid = fork()) == 0) {
								generation++;
								signal(SIGABRT, sigAbnorm);
								while(1) {
									//Processes do nothing.
									pause();
								}
							}
							childProcs.push_back(pid); //Store grandchild PID.
						} else if(op[1]) { //Goes here to process--.
							kill(childProcs.back(), SIGINT); //Kill last child made.
							childProcs.pop_back(); //Remove from list.
						}
						op[0] = op[1] = 0; //Reset opcode.
					}
				} else if(pid) {
					//Master-parent stores all info into his hash.
					server s;
					s.minProcs = min;
					s.maxProcs = max;
					s.numActive = min;
					s.pid = pid;
					hash[std::string(servName)] = s;
				}
			}
		} else if(strcmp(cmd, "abortServer") == 0) { //If aborting server, and it exists, destroy everything from that server onward.
			char *servName = strtok(NULL, " ");
			servName[strcspn(servName, "\n")] = '\0'; //Gets rid of \n char.
			std::string name = std::string(servName);
			if(hash.count(name)) { //Check if server even exists.
				cout << "Terminated server " << name << ".\n";
				kill(hash[name].pid, SIGINT); //Terminating custom signal.
				hash.erase(name); //Remove deleted server from list.
			} else {
				cout << "Enter valid server name. \n";
			}
		} else if(strcmp(cmd, "createProcess") == 0) { //Check if we're exceeding maximum, if not, signal to server for a new process.
			char *servName = strtok(NULL, " ");
			servName[strcspn(servName, "\n")] = '\0'; //Gets rid of \n char.
			std::string name = std::string(servName);
			if(hash.count(name) && hash[name].maxProcs > hash[name].numActive) { //Check if server exists, and not surpassing max.
				cout << "Created a new process for " << name << " server. \n";
				hash[name].numActive++; //Increment numActive for next reference.
				kill(hash[name].pid, SIGUSR1);
			} else {
				cout << "Check server name, or if another process can be created.\n";
			}
		} else if(strcmp(cmd, "abortProcess") == 0) { //Check if we're going below minimum, if not, signal to server to destroy a process.
			char *servName = strtok(NULL, " ");
			servName[strcspn(servName, "\n")] = '\0'; //Gets rid of \n char.
			std::string name = std::string(servName);
			if(hash.count(name) && hash[name].minProcs < hash[name].numActive) { //Check if server exists, and not falling below min.
				cout << "Terminated a process from " << name << " server. \n";
				hash[name].numActive--; //Decrement numActive for next reference.
				kill(hash[name].pid, SIGUSR2);
			} else {
				cout << "Check server name, or if another process can be terminated.\n";
			}
		} else if(strcmp(strtok(input, "\n"), "displayStatus") == 0) { //Just print our a 'tree-like' structure our family of processes.
			cout << "_CONTROL_SYSTEM_\n";
			vector<std::string> v;
			//This loop will attain the keys AKA server names.
			for(map<std::string,server>::iterator it = hash.begin(); it != hash.end(); ++it) {
  				v.push_back(it->first);
  				cout << "    \\_" << it->first << "_(" << hash[it->first].minProcs << "," << hash[it->first].maxProcs << ")" << "_\n";
				int totProcs = hash[it->first].numActive;
				//This loop will attain the processes.
				for(int i=0; i<totProcs; i++) {
					cout << "\t \\_P" << i << "_\n";
				}
				cout << "\n";
			}
		} else {
			cout << "Enter valid input.\n";
		}
	}

    return 0;
}

/*************************************************************
* Sends own processes PID to their parent process through a  *
* pipe.														 *
*************************************************************/
void sendPID() {
	dup2(fd[1], STDOUT_FILENO);
	char pid[8];
	sprintf(pid,"%ld", (long)getpid()); //Converts to string.
	write(STDOUT_FILENO, (const void *)pid, (size_t)strlen(pid) + 1);
}

/*************************************************************
* KEY: GENERATION 0 -> Parent process controlling servers.   *
* 	   GENERATION 1 -> Server process controlling processes. *
*  	   GENERATION 2 -> Process of a server.				 	 *
*************************************************************/
void sigExit(int sigNum) {
	if(generation == 0) {
		cout << " Shutting everything down, gracefully.\n";
		exit(0);
	} else if(generation == 1) {
		for(int i=0; i < (int)childProcs.size(); i++) { //Kill all server processes.
			kill(childProcs[i], SIGINT);
		}
		exit(0); //Self-destruct after grandchildren are dead.
	} else if(generation == 2) {
		exit(0); //Silently die.
	}
}

/*************************************************************
* KEY: OP 01 -> Abort on a process.						     *
* 	   OP 10 -> Create a new process. 						 *
*  	   OP 11 -> Process crashed abnormally.  Replace it. 	 *
*************************************************************/
void sigAbnorm(int sigNum) {
	sendPID(); //Since we're about to die, send our PID to our parent.
	kill(getppid(), SIGUSR2); //Triggering both signals causes a special op to take place.
	kill(getppid(), SIGUSR1); 
	exit(0);
}

void sigIncrease(int sigNum) {
	op[0] = 1; //Opcode for the server to increase processes.
}

void sigDecrease(int sigNum) {
	op[1] = 1; //Opcode for the server to decrease processes.
} 

