#!/bin/bash

mkdir lab0
cd lab0

mkdir -p emboar4/treecko
mkdir emboar4/azurill
touch emboar4/kirlia
mkdir emboar4/staryu
touch emboar4/vileplume
mkdir emboar4/hippopotas

touch gothorita6
touch happiny7
touch reuniclus1

mkdir -p slowbro3/cherubi
mkdir slowbro3/noctowl
touch slowbro3/chancey
touch slowbro3/klink
touch slowbro3/arcanine
touch slowbro3/elekid

mkdir -p togepi9/houndour
mkdir togepi9/delcatty
touch togepi9/swampert
mkdir togepi9/nuzleaf

echo -e "weigth=44.5 height=31.0 atk=4 def=4\nvileplume:\nСпособности\nPetal Dance Solarbeam" > emboar4/kirlia
echo -e "Тип покемона  PSYCHIC\nNONE" > gothorita6
echo -e "Тип диеты  Herbivore" > happiny7
echo -e "Тип покемона  PSYCHIC\nNONE" > reuniclus1
echo -e "Возможности  Overland=6 Surface=2 Jump=2 Power=2\nIntelligence=4" > slowbro3/chancey
echo -e "weigth=46.3 height=12.0 atk=6\ndef=7" > slowbro3/klink
echo -e "Тип диеты  Carnivore" > slowbro3/arcanine
echo -e "Живет  Forest Grassland\nUrban" > slowbro3/elekid
echo -e "weigth=180.6 height=59.0 atk=11 def=9" > togepi9/swampert

chmod 330 emboar4
chmod 305 emboar4/treecko
chmod 311 emboar4/azurill
chmod 062 emboar4/kirlia
chmod 311 emboar4/staryu
chmod 640 emboar4/vileplume
chmod 555 emboar4/hippopotas
chmod 006 gothorita6
chmod 002 happiny7
chmod 640 reuniclus1
chmod 753 slowbro3
chmod 770 slowbro3/cherubi
chmod 737 slowbro3/noctowl
chmod 444 slowbro3/chancey
chmod 620 slowbro3/klink
chmod 440 slowbro3/arcanine
chmod 404 slowbro3/elekid
chmod 311 togepi9
chmod 751 togepi9/houndour
chmod 570 togepi9/delcatty
chmod 046 togepi9/swampert
chmod 373 togepi9/nuzleaf

chmod 777 togepi9/swampert
cat togepi9/swampert slowbro3/arcanine > gothorita6_27
chmod 046 togepi9/swampert

chmod 777 happiny7
chmod 777 emboar4/hippopotas/
cp happiny7 emboar4/hippopotas
cp happiny7 togepi9/swamperthappiny
chmod 555 emboar4/hippopotas
chmod 002 happiny7

ln -s slowbro3 Copy_99
cp -r slowbro3 emboar4/treecko
ln reuniclus1 emboar4/kirliareuniclus
ln -s gothorita6 slowbro3/arcaninegothorita

echo -e "4.1"
wc -c $(ls -R1F | grep -v -e "^\./" -e "/$" -e "@&" | grep "^h") | head -n 2| sort -n 2>&1
echo -e "\n4.2"

#ls - u - сортровка по дате последнего доступа
ls -R1ul | grep "le" | grep "^-" | head -n 3 2>/tmp/opd_lab1_errors.log
echo -e "\n4.3"

cat -n $(ls -1dF **/e* | grep -v -e "/$" -e "@$") | sort -k 2 2>&1
echo -e "\n4.4"

ls -R1l | grep "ki" | grep "^-" | head -n 4 | sort -k 9 -r 2>/tmp/opd_lab1_errors.log
echo -e "\n4.5"

wc -l $(ls -1dF **/*e | grep -v -e "/$" -e "@$") | sort -nr 2>/tmp/opd_lab1_errors.log
echo -e "\n4.6"

#ls -t - сортировка по дате последней модификации
ls -lt emboar4 2>/tmp/opd_lab1_errors.log

rm -f gothorita6
rm -f emboar4/vileplume
rm -f slowbro3/arcaninegothori*
rm -f emboar4/kirliareunicl*
chmod -R 777 togepi9/
rm -rf togepi9/
rm -rf emboar4/hippopotas/






