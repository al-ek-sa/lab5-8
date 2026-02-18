#!/bin/bash
sed -i '/export WORKER_FILE=/d' ~/.bashrc
sed -i '/export HISTORY_FILE=/d' ~/.bashrc
echo 'export WORKER_FILE="/home/xfiles/itmo/proga/2sem/lab5/data/workers.csv"' >> ~/.bashrc
echo 'export HISTORY_FILE="/home/xfiles/itmo/proga/2sem/lab5/data/history.csv"' >> ~/.bashrc
echo 'export ID_FILE="/home/xfiles/itmo/proga/2sem/lab5/data/id.csv"' >> ~/.bashrc
source ~/.bashrc
