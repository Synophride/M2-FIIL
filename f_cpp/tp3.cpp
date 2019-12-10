#include <iostream>
#include <string>
#include <vector>
#include <memory>

class node;
typedef std::shared_ptr<node> node_ptr;

//graphes dirigés 
class node : std::enable_shared_from_this<node> {

public:
    node(std::string s){
	label = s;
	children = std::vector<node_ptr>();
	parents = std::vector<std::weak_ptr<node>>(); 
    }
    
    ~node(){
	std::cout<< "Destruction du noeud " << label << std::endl;
    }

    void add_child(node_ptr c){
	children.push_back(c);
	c->add_parent(shared_from_this());
    }


private:
    void add_parent(std::weak_ptr<node> n){
	parents.push_back(n);
    }
    std::string label;
    std::vector<std::weak_ptr<node>> parents;
    std::vector<node_ptr> children;
    // Sans pointeurs, ce serait des arbres 
};
    
int main(){
    node_ptr a(new node("a"));
    node_ptr b(new node("b"));
    node_ptr c(new node("c"));
    node_ptr d(new node("d"));

    a-> add_child(b);
    a-> add_child(c);
    d -> add_child(b);
    b-> add_child(a);
    return 0;

}
