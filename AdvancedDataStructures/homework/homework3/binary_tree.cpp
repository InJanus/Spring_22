#include "binary_tree.h"
#include <iostream>

binary_tree::binary_tree(){
    head = nullptr;
}

binary_tree::~binary_tree(){
    // destructor
    std::cout << "DESTRUCTOR!" << std::endl;
    delete_tree(head);
    
}

void binary_tree::delete_tree(node* mynode){
    if(mynode != nullptr){
        delete_tree(mynode->left);
        delete_tree(mynode->right);
        delete mynode;
    }
}

std::string binary_tree::print_tree(){
    return inorder_traversial(head);
}

std::string binary_tree::inorder_traversial(node* mynode){
    if(mynode){
        return 
        inorder_traversial(mynode->left) + 
        mynode->lname + ", " + 
        mynode->fname + ", " +
        mynode->pnumber + "\n" + 
        inorder_traversial(mynode->right);
    }
    return "";
}

std::string binary_tree::preorder_traversial(node* mynode){
        if(mynode){
        return 
        mynode->lname + "," + 
        mynode->fname + "," +
        mynode->pnumber + "\n" + 
        preorder_traversial(mynode->left) + 
        preorder_traversial(mynode->right);
    }
    return "";
}

void binary_tree::add_node(std::string myfname, std::string mylname, std::string mypnumber){
    if(head == nullptr){
        head = new node;
        head->fname = myfname;
        head->lname = mylname;
        head->pnumber = mypnumber;
    }else{
        node* temp = head;
        bool insert_flag = false;
        while(!insert_flag){
            // std::cout << temp->lname.compare(mylname) << std::endl;
            if(temp->lname.compare(mylname) > 0){
                // the value of the insert string is lower in value then go to the left
                if(temp->left == nullptr){
                    // insert to the left
                    temp->left = new node;
                    temp->left->fname = myfname;
                    temp->left->lname = mylname;
                    temp->left->pnumber = mypnumber;
                    insert_flag = true;
                }else{
                    temp = temp->left;
                }
            }else{
                if(temp->right == nullptr){
                    // insert to the right
                    temp->right = new node;
                    temp->right->fname = myfname;
                    temp->right->lname = mylname;
                    temp->right->pnumber = mypnumber;
                    insert_flag = true;
                }else{
                    temp = temp->right;
                }
            }
        }
    }
}

void binary_tree::delete_node(std::string myfname, std::string mylname){
    if(head){
        node* temp = head;
        node* parent = nullptr;
        bool delete_flag = false;
        while(!delete_flag){
            if(temp->lname.compare(mylname) > 0){
                parent = temp;
                temp = temp->left;
            }else if(temp->lname.compare(mylname) < 0){
                parent = temp;
                temp = temp->right;
            }else{
                // the strings are the same. check with the first name
                if(temp->fname.compare(myfname) == 0){
                    //both strings are equal
                    delete_flag = true;
                    std::cout << "found node!" << std::endl;
                    if(temp->left != nullptr && temp->right == nullptr){
                        
                    }else if(temp->left == nullptr && temp->right != nullptr){
                        if(parent->left == temp){
                            parent->left = temp->right;
                        }else if(parent->right == temp){
                            parent->right = temp->right;
                        }
                        delete temp;
                    }else if(temp->left != nullptr && temp->right != nullptr){
                        std::cout << "found node! again" << std::endl;
                        if(parent->left == temp){
                            node* temptemp = temp->left;
                            while(!temptemp->right){
                                temptemp = temptemp->right;
                            }
                            parent->left = temptemp;
                            temptemp->left = temp->left;
                        }else if(parent->right == temp){
                            node* temptemp = temp->right;
                            while(!temptemp->right){
                                temptemp = temptemp->left;
                            }
                            parent->right = temptemp;
                            temptemp->right = temp->right;
                        }else{
                            //parent is null therefore temp is root
                            temp->left->right = temp->right;
                            head = temp->left;
                        }
                        delete temp;
                    }else if(temp->left == nullptr && temp->right == nullptr){
                        
                        if(parent->left == temp){
                            parent->left = nullptr;
                        }else{
                            parent->right = nullptr;
                        }
                        delete temp;
                    }
                }else{
                    //freak out!
                }
            }
        }
    }
}