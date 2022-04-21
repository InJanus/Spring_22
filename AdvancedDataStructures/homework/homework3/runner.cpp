#include <iostream>
#include <string>

#include "binary_tree.h"

using namespace std;

int main(){
    binary_tree mytree;
    cout << mytree.print_tree() << endl;
    mytree.add_node("Brian", "Culberson", "5139677960");
    mytree.add_node("Greg", "Barker", "1234567890");
    mytree.add_node("Mugi", "Patel", "9876543210");
    cout << mytree.print_tree() << endl;
    mytree.delete_node("Brian", "Culberson");
    cout << mytree.print_tree() << endl;
    return 0;
}


