#!/bin/bash
sed -i '/export WORKER_FILE=/d' ~/.bashrc
sed -i '/export HISTORY_FILE=/d' ~/.bashrc
echo 'export WORKER_FILE="$HOME/lab5/data/workers.csv"' >> ~/.bashrc
echo 'export HISTORY_FILE="$HOME/lab5/data/history.csv"' >> ~/.bashrc
echo 'export ID_FILE="$HOME/lab5/data/id.csv"' >> ~/.bashrc
source ~/.bashrc
