FROM python:3.8-slim-buster

## workdir as current directory
WORKDIR /app

COPY . /app

RUN pip install -r requirements.txt

CMD ["python", "start.sh"]