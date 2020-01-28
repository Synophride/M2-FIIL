#include<algorithm>
#include<cassert>
#include<iostream>
#include<vector>
#include<string> 
#include<map>
#include<istream> 
#include<fstream>

typedef std::map<std::string, std::string> codes;
typedef std::map<std::string, int> occurences;
typedef std::multimap<int, codes> partial_codes;


std::vector<std::string> load( std::string filename ){
    std::vector<std::string> ret = std::vector<std::string> () ;
    
    std::ifstream in = std::ifstream(filename);
    while(! in.eof()){
	std::string s;
	std::getline(in, s);
	ret.push_back(s);
    }
    return ret;
}

occurences count(std::vector<std::string> const & s_vect){
    occurences r = std::map<std::string, int>();

    auto f = [&r](std::string s){
	auto paire = r.find(s);
	if(paire == r.end())
	    r[s] = 1;
	else
	    r[s]++;
    };

    std::for_each(s_vect.begin(), s_vect.end(), f);
    return r;
}


codes merge(codes const & c1, codes const & c2){
    codes r = std::map<std::string, std::string>();
    std::string s = "0";
    
    auto f = [&r, s]( std::pair<std::string, std::string> p){
	std::string key  = std::get<0>(p);
	std::string value= std::get<1>(p);
	r[key] = s + value;
    };
    
    std::for_each(c1.begin(), c1.end(), f);
    s = "1";
    std::for_each(c2.begin(), c2.end(), f);
    return r;
}

// Pue du cul
void reduce(partial_codes& pc){

    auto it = pc.lower_bound(-1);

    if(it == pc.end())
	assert(false);

    std::pair <int, codes > p  = (*it);

    int somme= std::get<0>(p);
    codes c1 = std::get<1>(p);
    pc.erase(it);


    
    it = pc.lower_bound(-1);
    if(it == pc.end())
	assert(false);
    
    p = *it;
    
    somme +=std::get<0>(p);
    
    codes c2 = std::get<1>(p);
    pc.erase(it);

    codes merged_code = merge(c1, c2);

    
    pc.insert(std::make_pair(somme, merged_code));

    return;
}

codes create(occurences occs){
    partial_codes pc = partial_codes();
    return codes();
}
int main(){
    auto v = load("./macbeth.txt");
    std::cout << v.size() << '\t' << v.back() << '\n';
    auto counter = count(v);    
    std::cout << counter["the"] << '\t'<< counter["macbeth"] << '\t'<< counter["chestnuts"] << '\t' << '\n';

    
}
