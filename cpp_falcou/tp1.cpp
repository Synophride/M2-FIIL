#include<cmath>
#include<iostream>
#include<cstdlib>
#include<cassert>
#include<cstdio> 
#include<vector>


typedef unsigned int uint; 
struct coord {
    double x, y;
};


coord mk_point(){
    coord ret;
    ret.x = ((double)std::rand())/((double)RAND_MAX);
    ret.y = ((double)std::rand())/((double) RAND_MAX);
    return ret;
}


std::vector<coord> mk_coord_vect(uint n){
    std::vector<coord> ret = std::vector<coord>();
    for(uint i = 0; i<n; i++)
	ret.push_back(mk_point());	    
    return ret;
}

double distance(coord a, coord b){
    double dist_x = b.x - a.x,
	dist_y = b.y - a.y;
    return sqrt((dist_x * dist_x) + (dist_y * dist_y));
}


int plus_proche(coord c, std::vector<coord> t){
    if (t.size() == 0)
	return -1; 

    double min_dist = distance(c, t[0]);
    uint indice_min = 0;

    for(uint i = 1; i < t.size(); i++){
	double d = distance(t[i], c);
	if(d < min_dist){
	    indice_min = i;
	    min_dist = d;
	}
    }
    return indice_min; 
}

void print_coord(const coord & c){
    std::cout << "( " <<c.x << ", " << c.y << ")\n";
}

void print_coord_vect(const std::vector<coord> & v){
    for(uint i =0; i<v.size(); i++)
	print_coord(v[i]);
	
    std::cout << std::endl;
}

void petits_tests();

//////////////////////////////////////////
//////          barytruc            //////
//////////////////////////////////////////

struct point {
    double x, y, poids;
};

point mk_pointp(){
    point ret;
    ret.x = ((double)std::rand())/((double)RAND_MAX);
    ret.y = ((double)std::rand())/((double) RAND_MAX);
    ret.poids = ((double)std::rand())/((double) RAND_MAX);
    return ret;
}
void print_point(point const &p){
    std::cout << "( " <<  p.x << ", " << p.y << " ) [" << p.poids << "] " << std::endl;
}

void print_point_vect(std::vector<point> const &v){
    for (auto p : v)
	print_point(p);
}
		     
coord barycentre(std::vector<point> const &v){
    double sum_x = 0., sum_y = 0., sum_poids = 0.;
    coord ret;
    for(auto const& p : v){
	sum_x += p.x*p.poids;
	sum_y += p.y*p.poids;
	sum_poids += p.poids;
    }
    
    ret.x = sum_x / sum_poids;
    ret.y = sum_y / sum_poids;
    return ret;
}


std::vector<point> mk_pointp_vect(uint n){
    std::vector<point> ret = std::vector<point>();
    for(uint i = 0; i<n; i++)
	ret.push_back(mk_pointp());
    return ret;
}

void petits_test_ii();
/////////////////////////////////////////

int main(int argc, char** args){
    srand(0);
    
    petits_tests();
    petits_test_ii();
    return 0;
}

void petits_tests(){
    coord c1 = {0,0}, c2 = {1,1};
    print_coord(c1);
    print_coord(c2);
    std::cout << std::endl << distance(c1, c2) << std::endl;
    auto v = mk_coord_vect(25);
    std::cout << "Vecteur hasardeux" << std::endl;
    print_coord_vect(v);
    std::cout << "Calcul du point le plus proche de 0,0" << std::endl;
    print_coord(v[plus_proche(c1, v)]);
    std::cout << "Calcul du point le plus proche de (1,1)" << std::endl;
    print_coord(v[plus_proche(c2, v)]);
    
}

void petits_test_ii(){
    print_coord(barycentre(mk_pointp_vect(3)));
    std::cout<<"banane" << std::endl;
    print_coord(barycentre(mk_pointp_vect(3501)));
    
    
}
