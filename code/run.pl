#!/usr/bin/perl -w

$cmd = "javac *.java";
system($cmd);
$time = 10;
@seeds = (123456789, 987654321, 147258369, 369258147, 789456123, 
    456123789, 258369147, 369147258, 159264837, 849516237);

@attacks = ("jam", "fdi-load", "fdi-time");
@percentage = (5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

foreach $att (@attacks){
  `printf "%10s,%10s,%10s,%10s\n" percent baseline before after > $att.txt`  
}

foreach my $per (@percentage) {
  foreach my $att (@attacks){
    foreach my $seed (@seeds){
      $cmd = "java Simulator " . $time . " " . $seed . " " . $att . " " . $per . " >> " . $att . ".txt";
      print $cmd . "\n";
      system($cmd);
    }
  }
}
