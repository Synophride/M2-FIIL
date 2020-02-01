#include<iterator>
#include<algorithm>
#include<iostream>
#include<vector>
int N = 150;
///// Exercice 1
namespace exo1 {
    namespace q1 {    
	template<typename T>
	std::ostream &operator<<(std::ostream &out,
				 std::vector<T> const& v){
	
	    out << "[ " ;
	    for(size_t i = 0; i<v.size()-1; ++i){
		out << v[i] << ", ";
	    }
	    out << v[v.size()-1];
	    out << " ]\n";
	    return out;
	}
    
	void test(){
	    std::vector<int> v = std::vector<int>();
	    for(int i = 0; i<N; ++i)
		v.push_back(2*i);
	    std::cout << v;
	}
    }


    namespace q2 {
	template<typename T>
	std::ostream &operator<<(std::ostream &out,
				 std::vector<T> const& v){
	    out << "[ ";
	    auto it = std::ostream_iterator<T>(out, " ,");
	    std::copy( v.begin(), v.end(), it );
	    out << " ]\n";
	    return out;
	}
    

	void test(){
	    std::vector<int> v = std::vector<int>();
	    for(int i = 0; i<N; ++i)
		v.push_back(2*i);
	    std::cout << v;
	}
    }
/*
  namespace q3 {
  template<typename T> struct pretty_iterator :
  std::iterator<std::output_iterator_tag, void, void, void, void>
  {
  std::ostream& r_out;
  bool fst;
  const char r_delim;
  // tpnote.cpp:60:2: error: uninitialized reference member in
  // ‘std::ostream& {aka class std::basic_ostream<char>&}’ [-fpermissive]
  pretty_iterator<T>(std::ostream &out, char const delim) {
  r_out = out; 
  r_delim = delim;
  fst = true;
  };

  pretty_iterator &operator=(T const &t){
  if(! fst)
  r_out << r_delim << t << " ";
  else {
  r_out << t << " "; 
  }
  fst = false;
  }
	
  pretty_iterator &operator*(){return *this;}
  pretty_iterator &operator++(){return *this;}	    
  };
    
  template<typename T>
  std::ostream &operator<<(std::ostream &out,
  std::vector<T> const& v){
  out << "[ ";
  auto it = pretty_iterator<T>(out, ',');
  std::copy( v.begin(), v.end(), it );
  out << " ]\n";
  return out;
  }
  void test(){
  std::vector<int> v = std::vector<int>();
  for(int i = 0; i<N; ++i)
  v.push_back(2*i);
  std::cout << v;
  }
  }
*/
}
//// Exo 2
//

const int max_random = 2;
const int taille_tableau = 14;
namespace exo2{
    namespace q1{
	std::vector<int> random(size_t n, int max){
	    std::vector<int> retour = std::vector<int>();
	    for(size_t i = 0; i<n; ++i)
		retour.push_back((rand()%(max * 2 + 1)) - max);
	
	    return retour;
	}
	void test(){
	    auto v = random(taille_tableau, max_random);
	    for(auto e : v)
		std::cout << e << ", ";
	
	    std::cout << "\n" ;
	}
    }

    namespace q2{
	std::vector<int> random(size_t n, int max){
	    std::vector<int> retour = std::vector<int>();
		
	    auto f = [max](){
		return(rand()%(max * 2 + 1)) - max;
	    };;

	    auto it = std::back_insert_iterator<std::vector<int>>(retour);
	    
	    std::generate_n(it, n, f);
	    return retour;
	}
	void test(){
	    auto v = random(taille_tableau, max_random);
	    for(auto e : v)
		std::cout << e << ", ";
	
	    std::cout << "\n" ;
	}
    }

    namespace q3{
	struct rand_iterator:
	    std::iterator<std::input_iterator_tag, int, void>
	{
	    int r_max;
	    int currval;
	    int getVal(){
		return(rand()%(r_max * 2 + 1)) - r_max;
	    }
	
	    rand_iterator(int max){
		r_max = max;
		currval = getVal();
	    }
	
	    int operator*(){
		currval=getVal(); 
		return currval;
	    }
	
	    rand_iterator &operator++(){
		return *this;
	    }
	};
	
	std::vector<int> random(size_t n, int max){
	    std::vector<int> retour = std::vector<int>();
	    auto bii = std::back_insert_iterator<std::vector<int>>(retour);
	    auto ri = rand_iterator(max);
	    std::copy_n(ri, n, bii);
	
	    return retour;
	}

	void test(){
	    auto v = random(taille_tableau, max_random);
	    for(auto e : v)
		std::cout << e << ", ";
	
	    std::cout << "\n" ;
	}
    }
}
namespace exo3 {
    namespace q1 {
	
	int non_min(std::vector<int> const &v){
	    int i = 0;
	    auto f = [i](int val)
		{return val == i;};
	    
	    for(i = 0; i<(int)v.size(); ++i)
	        if(v.end() == std::find_if(v.begin(), v.end(), f) )
		    return i;
	    return i;
	}

	void test(){
	    auto v = exo2::q1::random(15, 12);
	    for(auto i : v)
		std::cout << i << ", ";
	    std::cout <<  "\n --> non min = " << non_min(v) << "\n";
	}
    }
    
    namespace q2{
	
    }
}

int main(){
    std::cout << "#### Exo 1 #####\n"; 
    std::cout << "quesiton 1 \n";
    exo1::q1::test();
    std::cout << "question 2 \n";
    exo1::q2::test();
    std::cout << "question 3 \n";
    // Ne compile pas
    std::cout << "#### Exo 2 ####\n";
    std::cout << "question 1 \n";
    exo2::q1::test();
    std::cout << "question 2 \n";
    exo2::q2::test();
    std::cout << "question 3 \n";
    exo2::q3::test();

    std::cout << "#### Exo 3 #####\n"; 
    std::cout << "question 1 \n";
    exo3::q1::test(); // Ne fonctionne pas 
    std::cout << "question 2 \n";

    std::cout << "question 3 \n";

}
