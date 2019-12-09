#include<iostream> 


class T {
public:
  T(){std::cout<<this<<": constructed from nothing"<<std::endl;}
  T( T const &other){
    std::cout << this << ": constructed from " << &other << std::endl;
    
  } 
  
  ~T(){std::cout << this << " destructed " << std::endl;};

  T& operator=(T other){
    std::cout << this << " <- " << &other << std::endl;
    return other; 
  }  
};


// Interdiction d'utiliser un objet après un move : on doit considérer
// Les objets passés à move comme perdus
void f1(T const &t){}
void f2(T t) {}
T f3() { return T();}
T f4() {T t; return t;}
void f5(T &t){ t = T();}



/*struct U{
  T v1, v2;
  U ( T const &t) : fv2(t){v1 = t;
  };*/
int main(){
  T a;
  f1(a);
  f2(a);
  T b = a ;
  T c = f3 () ;
  T d = f4 ();
  f5(d);
  //  U e (a) ;

}
