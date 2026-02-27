# 🚀 Java DevOps Pipeline — Capstone Project

A production-style DevOps pipeline that automatically builds, containerizes, and deploys a Java Spring Boot application to Kubernetes on AWS — with all infrastructure provisioned as code using Terraform.

---

## 📐 Architecture

```
Developer pushes code
        ↓
    GitHub Repo
        ↓
   Maven Build
   (mvn package)
        ↓
  Docker Build
  (multi-stage)
        ↓
   AWS ECR
 (image stored)
        ↓
Kubernetes on AWS EKS
  (app deployed)
        ↓
 AWS Load Balancer
 (publicly accessible)
```

---

## 🛠️ Tech Stack

| Tool | Purpose |
|------|---------|
| Java 21 + Spring Boot | REST API application |
| Maven | Build tool |
| Docker | Containerization (multi-stage build) |
| AWS ECR | Private Docker image registry |
| Terraform | Infrastructure as Code (IaC) |
| AWS EKS | Managed Kubernetes cluster |
| Kubernetes | Container orchestration |
| AWS S3 + DynamoDB | Terraform remote state + locking |
| AWS VPC | Network isolation for the cluster |

---

## 📁 Project Structure

```
my-devops-project/
├── src/                          # Java Spring Boot source code
│   └── main/java/.../
│       └── AppController.java    # REST controller
├── pom.xml                       # Maven build config
├── Dockerfile                    # Multi-stage Docker build
├── terraform/                    # Infrastructure as Code
│   ├── main.tf                   # EKS + VPC modules
│   ├── providers.tf              # AWS provider + S3 backend
│   ├── outputs.tf                # Cluster endpoint + name
│   └── variables.tf              # Input variables
└── k8s/                          # Kubernetes manifests
    ├── deployment.yaml           # App deployment (2 replicas)
    └── service.yaml              # LoadBalancer service
```

---

## ☁️ Infrastructure (Terraform)

All AWS infrastructure is provisioned using Terraform:

- **VPC** — Custom VPC with public and private subnets across 2 availability zones
- **NAT Gateway** — Allows private subnet nodes to pull images from ECR
- **EKS Cluster** — Managed Kubernetes control plane (v1.31)
- **Node Group** — EC2 t3.medium worker nodes (auto-scaling 1-2)
- **Remote State** — Stored in S3 bucket with DynamoDB locking for team safety

---

## 🐳 Docker — Multi-Stage Build

The Dockerfile uses a multi-stage build for a smaller, production-ready image:

- **Stage 1 (build)** — Uses `maven:3.9-eclipse-temurin-21` to compile and package the JAR
- **Stage 2 (runtime)** — Uses `eclipse-temurin:21-jre-alpine` (minimal JRE only, no build tools)

This keeps the final image small and secure — no Maven or JDK in production.

---

## ☸️ Kubernetes

The app runs on AWS EKS with:

- **2 replicas** for high availability
- **Liveness probe** on `/actuator/health` — K8s auto-restarts unhealthy pods
- **LoadBalancer service** — AWS automatically provisions an ELB for public access
- **Rolling updates** — Zero downtime deployments on new image push

---

## 🚀 How to Run

### Prerequisites
- AWS CLI configured
- Terraform installed
- kubectl installed
- Docker installed

### Step 1 — Provision Infrastructure
```bash
cd terraform
terraform init
terraform apply
```

### Step 2 — Connect kubectl to EKS
```bash
aws eks update-kubeconfig --name my-eks-cluster --region us-east-1
kubectl get nodes
```

### Step 3 — Build & Push Docker Image
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 853766429659.dkr.ecr.us-east-1.amazonaws.com
docker build -t my-java-app .
docker tag my-java-app:latest 853766429659.dkr.ecr.us-east-1.amazonaws.com/my-java-app:latest
docker push 853766429659.dkr.ecr.us-east-1.amazonaws.com/my-java-app:latest
```

### Step 4 — Deploy to Kubernetes
```bash
kubectl apply -f k8s/
kubectl get pods
kubectl get svc myapp-service
```

### Step 5 — Access the App
```
http://<EXTERNAL-IP>/hello
```

### Cleanup (to avoid AWS charges)
```bash
kubectl delete -f k8s/
cd terraform && terraform destroy
```

---

## 🔑 Key Concepts Demonstrated

- **Infrastructure as Code** — entire AWS infrastructure defined in Terraform, version controlled in Git
- **Containerization** — app packaged as a Docker image, portable across any environment
- **Container Orchestration** — Kubernetes handles self-healing, scaling, and rolling updates automatically
- **Remote State Management** — Terraform state stored in S3 with DynamoDB locking for safe team collaboration
- **Multi-stage Docker builds** — smaller, more secure production images
- **Cloud-native deployment** — running on managed AWS services (EKS, ECR, ELB)

---

## 📊 What Kubernetes Does Automatically

| Feature | How |
|--------|-----|
| Self-healing | Restarts crashed pods automatically |
| Load balancing | Distributes traffic across 2 replicas |
| Health checks | Liveness probe on /actuator/health |
| Zero downtime deploy | Rolling update strategy |
| Auto scaling | Node group scales 1-2 EC2s based on load |

---

## 👨‍💻 Author

**Arjun**
- GitHub: [Star-codi](https://github.com/Star-codi)
- Project Repo: [Capstone-project](https://github.com/Star-codi/Capstone-project)