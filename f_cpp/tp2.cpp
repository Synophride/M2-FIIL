#include<iostream> 


class T {
  T(){
    std::cout << "Objet créé:\t" << this << std::endl;
  }

  T(T &other){
    std::cout << "Objet créé:\t" << this
	      << " à partir de " << &other << std::endl;
    *this=other; 
  }
  
  ~T(){
    std::cout << "Objet détruit:\t" << this << std::endl;
  }

  T& operator=(T& other){
    std::cout << this << " <- " << &other << std::endl;
    return other;
  }

  
};



void f1(T const &t) {}
void f2(T t) {} 

int main(){


} 
