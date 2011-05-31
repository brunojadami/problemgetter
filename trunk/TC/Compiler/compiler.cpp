/** 	
	PardalProcessor v1.2 for TopCoder - By Pardal Team
	Any suggestions or bugs: brunojadami@gmail.com
	Designed to use with CodeProcessor+moj+FileEdit 
**/
	
#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>

#define MUSTREAD "\033[36m\0"
#define SUCCESS "\033[32m\0"
#define WARNING "\033[33m\0"
#define ERROR "\033[31m\0"
#define RESET "\033[0m\0"
#define INTRO "\033[34m\0"
#define CLEAR "clear\0"
#define EXEC "./a.out\0"
#define COMP "g++ -Wall -g \0"

using namespace std;

int main(int argc, char** argv)
{
	system(CLEAR);
	
	cout << INTRO << "PardalProcessor v1.2 - By Pardal Team\n"
			 "Any suggestions or bugs: brunojadami@gmail.com\n" 
			 "Designed to use with CodeProcessor+moj+FileEdit\n" << RESET << endl;

	// Check for valid args
	if (argc < 2)
	{
		cout << ERROR << "Invalid arguments, usage: compiler source.cpp" << RESET << endl;
		return 0;
	}
	
	// Check and get source
	ifstream file(argv[1], ios::in|ios::binary|ios::ate);
	if (!file.is_open())
	{
		cout << ERROR << "Invalid file!" << RESET << endl;
		return 0;
	}
	size_t size = file.tellg();
    	char* memblock = new char[size];
    	file.seekg(0, ios::beg);
    	file.read(memblock, size);
    	file.close();
    	string source = memblock;
    	
    	cout << SUCCESS << "Source readed." << RESET << endl;
	
	size_t p = 0, t, x;
	
	// Remove description
	bool mod = false;
	p = source.find("// BEGIN CUT HERE");
	t = source.find("// END CUT HERE");
	if (p != string::npos && p != string::npos && source.find("PROBLEM STATEMENT") != string::npos)
	{
		source.replace(p + 18, t - p - 18, "");
		cout << SUCCESS << "Removed description." << RESET << endl;
		mod = true;
	}
	else
		cout << WARNING << "Problem description might be missing." << RESET << endl;
	if (p != 0)
		cout << WARNING << "Problem description is not on first line." << RESET << endl;
	
	// Finding macros and their usage
	int c = 0, m = 0;
	p = 0;
	while (1)
	{
		p = source.find("#define", p);
		// Getting macro
		if (p != string::npos)
		{
			++m;
			string macro;
			p += 8;
			for (x = p; ; ++x)
				if (source[x] == '(' || source[x] == ' ')
				{
					macro = source.substr(p, x - p);
					break;
				}
			// Finding its usage
			t = string::npos;
			while (1)
			{
				t = source.rfind(macro, t);
				if (t == p)
					t = source.find(macro, 0);
				if (t == p)
					break;
				char q = 0;
				if (t > 0)
					q = source[t-1];
				if (q >= 'A' && q <= 'Z') // Invalid usage
				{
					--t;
					continue;
				}
				if (t + macro.size() < source.size()-1)
					q = source[t + macro.size()];
				if (q >= 'A' && q <= 'Z') // Invalid usage
				{
					--t;
					continue;
				}
				break;
			}
			if (t == p)
			{
				// Useless, remove it
				mod = true;
				++c;
				for (t = x; ; ++t)
					if (source[t] == '\n')
						break;
				source.replace(p - 8, t - p + 9, "");
				p -= 8;
			}
			else
				++p;
		}
		else
			break;
	}
	if (m == 0)
		cout << WARNING << "There are no macros." << RESET << endl;
	else
		cout << SUCCESS << "Removed " << c << " unused macros." << RESET << endl;
	
	// Changing test colors
	string err = ERROR;
	err += "FAILED";
	err += RESET;
	p = 0;
	if (source.find(err) == string::npos)
	{
		while (1)
		{
			p = source.find("FAILED", p);
			if (p == string::npos)
				break;
			mod = true;
			source.replace(p, 6, err.c_str());
			p += err.size();
		}
	}
	string pas = SUCCESS;
	pas += "PASSED";
	pas += RESET;
	p = 0;
	if (source.find(pas) == string::npos)
	{
		while (1)
		{
			p = source.find("PASSED", p);
			if (p == string::npos)
				break;
			mod = true;
			source.replace(p, 6, pas.c_str());
			p += pas.size();
		}
	}
	cout << SUCCESS << "Changed test colors." << RESET << endl;
	
	// Fix file end
	for (x = source.size() - 1; ; --x)
		if (source[x] == 'E' || source[x] == '}') // From HER'E' or }
			break;
	++x;
	bool fe = false;
	if (source.size() - x > 1)
		fe = true;
	source.replace(x, source.size() - x, "");
	cout << SUCCESS << "Fixed file end." << RESET << endl;
	
	// Changing to always show time
	p = source.find("CLOCKS_PER_SEC / 200");
	if (p != string::npos)
	{
		mod = true;
		source.replace(p, 20, "-1");
	}
	
	mod |= fe;
	// Save old file and new file
	if (mod)
	{
		ofstream out(argv[1]);
		out.write(source.c_str(), source.size());
		out.close();
		string s = "Backup";
		s += argv[1];
		ifstream check(s.c_str());
		if (!check.is_open())
		{
			ofstream bak(s.c_str());
			bak.write(memblock, size);
			bak.close();
		}
		else
			check.close();
	}
	delete[] memblock;
	
	cout << SUCCESS << "Saved file and backup." << RESET << endl;
	
	// Compiling
	cout << SUCCESS << "Compiling..." << RESET << endl;
	
	string comp = COMP;
	comp += argv[1];
	if (system(comp.c_str()) != 0)
	{
		cout << ERROR << "Compilation failed, please fix errors and try again." << RESET << endl;
		if (mod)
			cout << MUSTREAD << "***You might need to reload the file on your editor!***" << RESET << endl;
		return 0;
	}
	else
		cout << SUCCESS << "Compilation successful, there may be warnings." << RESET << endl;
		
	// Running
	cout << SUCCESS << "Running..." << RESET << endl;
	system(EXEC);
	cout << SUCCESS << "TCcppProcessor finished!" << RESET << endl;
	if (mod)
		cout << MUSTREAD << "***You might need to reload the file on your editor!***" << RESET << endl;
	
	return 0;
}

