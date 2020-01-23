#include<iostream>
#include<cassert> 
template<typename T, std::size_t N>
class small_array{
public:
    small_array() = default;
    small_array(small_array const &) = default;
    small_array(small_array &&) = default;
    small_array &operator= (small_array const & ) = default;
    small_array &operator= (small_array &&) = default;
    ~small_array() = default;

    

    T& operator[](std::size_t i){
	assert(i<N);
	return _tab[i];
    }
    
    // opérateur pour les instances constantes ?
    T const &operator[](std::size_t i) const {
	printf("appel aux trucs constants\n");
	return _tab[i];
    }

private:
    T _tab[N];
};


template<typename T, std::size_t N>
class big_array{
    big_array(){

    }
private:
    std::unique_ptr<>;
}
int main () {
    small_array < int , 4 > t ;
    t [2] = 42;
    small_array < int , 4 > const u = t ;
    for ( std :: size_t i = 0; i < 4; ++ i ) {
	std :: cout << '[' << i << " ] = " << u [ i ] << '\n';
    }
    t [4] = 0; // assertion failed !
}
