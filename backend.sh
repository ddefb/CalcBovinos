#!/bin/sh
# 1- esse bash altera o index.html do report para colocar o commit id e link do commit
# 2- faz commit com uma deploy key para o projeto de reports no github

username='NutriCampus'
mainprojname='NutriCampus'
reportprojname='NutriCampusUnitTestReport'
githubmainproject='https://github.com/'$username'/'$mainprojname
githubreportproject='https://github.com/'$username'/'$reportprojname

#pathprojeto='/home/matt/IDEs_workspaces/AndroidStudioProjects/'$mainprojname'/' #comentar essa linha quando rodar no travis
pathprojeto='/home/travis/build/'$username'/'$mainprojname'/' #comentar essa linha quando rodar localmente

pathprojetoreport=$pathprojeto$reportprojname'/'
pathreport=$pathprojeto'app/build/reports/tests/testDebugUnitTest/'
pathindexfile=$pathreport'index.html'
pathindexfiletemp=$pathreport'temp'

#pego os dados do commit principal
commitidLONG=`cd $pathprojeto && git rev-parse HEAD`
commitidSHORT=`cd $pathprojeto && git rev-parse  --short HEAD`
ismaster=`cd $pathprojeto && git rev-parse --abbrev-ref HEAD`

#só faz o deployment do report se for o branch master que foi atualizado
if [ "$ismaster" = "master" ];
then
	#regex
	tofind1='<h1>Test Summary</h1>'
	tofind2='<div id="footer">'

	replacewith1='<h1>Test Summary</h1 <h4>main commit: <a href="'$githubmainproject'/commit/'$commitidLONG'" target="_blank">'$commitidSHORT'<a></h4>'
	replacewith2='<div id="footer"> Main project  @ <a href="'$githubmainproject'" target="_blank">'$mainprojname'</a><br /> Reports hosted @ <a href="'$githubreportproject'" target="_blank">'$reportprojname'</a>'
	#escrevo o conteudo no novo arquivo
	cat $pathindexfile | while read line
	do
		if [ "$line" = "$tofind1" ];
		then
			echo $replacewith1 >> $pathindexfiletemp
		elif [ "$line" = "$tofind2" ];
		then
			echo $replacewith2 >> $pathindexfiletemp
		else
			echo $line >> $pathindexfiletemp
		fi	
	done

	#passo para o arquivo original
	indexhtml=`cat $pathindexfiletemp`
	echo $indexhtml > $pathindexfile

	#excluo arquivo tmeporario
	rm $pathindexfiletemp

	#fazer clone na raiz
	cd $pathprojeto
	
	#apago conteúdo para alocar o novo conteúdo
	`rm -rf $pathprojetoreport`
	`git clone $githubreportproject'.git' && cd $pathprojetoreport && find . \! -name '.git'  \! -name 'README.md' -delete`

	#copiar arquivos
	cp -a $pathreport. $pathprojetoreport

	cd $pathprojetoreport

	#fazer commit com deploy key ou personal key
	#git add -A && git commit -m "Main commit: https://github.com/ddefb/NutriCampus/commit/"$commitidLONG && git push -u origin master
fi
