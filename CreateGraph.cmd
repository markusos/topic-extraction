for /f %%f in ('dir /b graphs') do del graphs\%%f

for /f %%f in ('dir /b dot') do C:\graphviz\bin\sfdp -Tsvg dot\%%f -o graphs\%%f.svg

for /f %%f in ('dir /b dot') do del dot\%%f

exit