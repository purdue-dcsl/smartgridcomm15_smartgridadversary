###############################################################################
# Nathan Burow
# 02/02/15
###############################################################################
TEXFILE:=report
OUTPUT:=report.pdf


all: pdf
	
pdf: 	
	pdflatex ${TEXFILE}.tex
	#- bibtex ${TEXFILE}.aux
	#pdflatex ${TEXFILE}.tex
	#pdflatex ${TEXFILE}.tex
	#mv ${TEXFILE}.pdf ${OUTPUT}

clean:
	rm -f *~ *.aux *.dvi *.log *.blg *.bbl *.toc *.lof *.lot *.out *.synctex.gz
