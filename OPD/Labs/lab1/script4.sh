#!/bin/bash

cd lab0

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
