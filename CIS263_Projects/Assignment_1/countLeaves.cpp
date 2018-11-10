#include "BinarySearchTree.h"
#include <iostream>
#include <string>

using namespace std;

int main()
{

  BinarySearchTree < int > *bst = new BinarySearchTree < int > ();

  while(std::cin) {
    int value;
    cin >> value;
    bst->insert(value);
  }
  cout << bst->countLeaves() << endl;
  delete (bst);
  return 0;
}
