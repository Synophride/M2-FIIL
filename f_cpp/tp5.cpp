#include<iostream>
#include<string>
#include<memory>

template<typename T, std::size_t N>
class small_array{
public:
  
  small_array<T, N>(){
    // Quelque chose à faire ? 
  }

  small_array<T, N>(const small_array<T, N> & other){
    for(int i = 0; i<N; ++i)
      _tab[i] = other[i];
  }

  small_array<T, N>(small_array<T, N> && other){
    _tab = std::move(other._tab);
  }

  small_array<T, N>& operator= (const small_array<T, N>& other){

    // P-ê qu'il faudrait delete les trucs courants 
    for(int i = 0; i<N; ++i){
      _tab[i] = other[i];
    }
  }
  
  T& operator[](std::size_t i){
    return _tab[i];
  }
  
  small_array<T, N>& operator= (small_array<T, N> && other){
    _tab = std::move(other._tab);
  }
  
  ~small_array<T, N> () {}
  
private:

  T _tab[N];
};


template<typename T, int N> class large_array{
public:
  large_array<T, N>(){
    _tab = std::unique_ptr< T[N] >(new T[N]);
  }
  
  large_array<T, N>(const large_array<T, N> & other){
    _tab = std::unique_ptr< T[N] >(new T[N]);

    std::unique_ptr<T[N]> other_ptr = std::unique_ptr<T[N]>(other._tab);
    for(int i = 0; i<N; ++i){
      (*_tab)[i] = T( (*other_ptr)[i] );
    }
  }

  large_array<T, N>(large_array<T, N> && other){
    _tab = std::move(other._tab);
  }

  large_array<T, N>& operator= (const large_array<T, N>& other){
    _tab = std::unique_ptr< T[N] >(new T[N]);
    // TODO 

  }
  
  large_array<T, N>& operator= (large_array<T, N> && other){
    _tab = std::move(other._tab);
  }

  ~large_array<T, N>(){
    tab.release();
  }
  
private:
  std::unique_ptr<T[N]> _tab;
  
};


int main(int argc, char** argv){

  for(int i = 0; i < argc; ++i)
    std::cout << argv[i] << '\n';
  
  return 0;
}
