#include<iostream>
#include<vector>
#include<cassert>
#include<utility>
#include<stdexcept>
#include<memory>
// #include<string> 
/************
 * EXERCICE 1
 ***********/


class matrix {
public:
    virtual std::pair<int, int> size() const = 0;
    virtual double get(int i, int j) const = 0;
    virtual ~matrix() = default;
    
};

class full_matrix : public matrix {
private:
    std::vector<std::vector<double>> r_v;
    unsigned int r_width;
    unsigned int r_height;
public:

    
    full_matrix(unsigned int height, unsigned int width, std::vector <double> const &v){
	r_height = 0;
	r_width = width;
	r_v = std::vector<std::vector<double>>();
	for(unsigned int i = 0; i<height; ++i){
	    r_v.push_back(std::vector<double>(v));
	    r_v[i].resize(width, 0);
	    r_height ++;
	}
    }

    // BASIC.
    // exemple d'exception : pas assez de mémoire lors du resize() 
    full_matrix(unsigned int height, unsigned int width, std::vector <double> &&v){
	r_height = 0;
	r_width = width;

	r_v = std::vector<std::vector<double>>();
	
	v.resize(width, 0);	
	for(unsigned int i = 0 ; i<height-1; ++i){
	    r_v.push_back(std::vector<double>(v));
	    r_height ++;
	}
	if(height > 0){
	    r_v.push_back(std::move(v));
	    r_height ++;
	}
    }
    
    std::pair<int, int> size() const noexcept{
	return std::pair<int, int>(r_height, r_width);
    }


    // STRONG (pas de modification et pas de corruption)
    // cas exceptionnel = la condition est évaluée à True
    double get(int i, int j) const {
        if(i<0 || i>=r_height || j<0 || j>=r_width){
	    throw new std::out_of_range("mauvaise bornes eugheur");
	}
	return(r_v[i][j]);
    }    
};


std::ostream& operator<<(std::ostream &out, matrix const &m){
    std::pair<int, int> size = m.size();
    int height = std::get<0>(size);
    int width  = std::get<1>(size);
    for(int i = 0; i<height; ++i){
	for(int j = 0; j<width; ++j){
	    out << m.get(i, j) << "\t";
	}
	out << "\n";
    }
    out << "\n";
    return out;
}


class diag_matrix : public matrix {
private:
    std::vector<double> r_v;
    unsigned int r_size;

public:
    diag_matrix(std::vector<double> const& v){
	r_size = 0;
	r_v = std::vector<double>(v);
	r_size = v.size();
    }
    // BASIC (s'il y a une exception lors de std::move) 
    diag_matrix(std::vector<double> && v){
	r_v = std::move(v);
	r_size = r_v.size();
    }

    std::pair<int, int> size() const noexcept {
	return std::pair<int, int>(r_size, r_size);
    }


    // STRONG (pas de modification et pas de corruption)
    // cas exceptionnel = la condition est évaluée à True
    double get(int i, int j) const {
        if(i<0 || i>= r_size || j<0 || j>=r_size){
	    throw new std::out_of_range("mauvaise bornes eugheur");
	} else if(i == j)
	    return r_v[i];
        else
	    return 0;
    }
};

    
int main(){
    full_matrix m(2, 4, {1., 2., 3., 4., 5., 6.});
    std::cout << m << '\n';

    std::shared_ptr<matrix> d(new diag_matrix({7.,8.,9.,10.,}));
    std::cout<< *d << '\n';
    return 0;
    
}