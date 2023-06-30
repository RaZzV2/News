docker compose up -d
sudo docker run -d --gpus all -p 5000:5000 --name imageSimilarity-ai-container razzv2/image-features-server
sudo docker run -d --gpus all -p 7000:7000 --name text-ai-container razzv2/text-similarity-ai
