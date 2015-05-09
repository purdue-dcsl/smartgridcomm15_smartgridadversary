#!/usr/bin/perl -w

$cmd = "javac *.java";
system($cmd);
$time = 10;
@seeds = (123456789, 987654321, 147258369, 369258147, 789456123, 
				  456123789, 258369147, 369147258, 159264837, 849516237);

@attacks = ("jam", "fdi-load", "fdi-time");

foreach my $att (@attacks){
	foreach my $seed (@seeds){
		$cmd = "java Simulator " . $time . " " . $seed . " " . $att . " >> " . $att . ".txt";
		print $cmd . "\n";
		system($cmd);
	}
}
