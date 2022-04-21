#ifndef BINARYTREE
#define BINARYTREE

#include <string>

struct node{
    node *left = nullptr;
    node *right = nullptr;
    std::string fname = "";
    std::string lname = "";
    std::string pnumber = "";
};

class binary_tree{
    public:
        binary_tree();
        ~binary_tree();
        void add_node(std::string myfname, std::string mylname, std::string mypnumber);
        void delete_node(std::string myfname, std::string mylname);
        std::string print_tree();
    private:
        node *head;
        void delete_tree(node* mynode);
        std::string inorder_traversial(node* mynode);
        std::string preorder_traversial(node* mynode);
        int compare(std::string string1, std::string string2);
};

#endif