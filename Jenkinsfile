pipeline {

    agent any

    tools {
        // 1. Kural: Global Tool Configuration'da Maven ve JDK'ya verdiğiniz isimlerin birebir aynısı olmalı
        maven 'Maven_3.9'
        jdk 'JDK21' // Eğer Jenkins'e JDK21'i bu isimle eklemediyseniz bu satırı tamamen silebilirsiniz (Jenkins varsayılan Java'yı kullanır)
    }

    stages {

        stage('Checkout') {
            steps {
                // 2. Kural: Jenkins arayüzünde SCM (Git) kullandığınız için bu aşamayı Jenkins zaten otomatik yapar.
                // Buraya kendi repo URL'nizi yazabilir veya Jenkins otomatik çektiği için bu stage'i tamamen kaldırabilirsiniz.
                git branch: 'main', url: 'https://github.com/fatihben/Crossref_Cucumber.git'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Generate Report') {
            steps {
                // 3. Kural: Cucumber plugin'inin çalışması için pom.xml'inizdeki Cucumber raporlama diziniyle burası uyuşmalı.
                // target/json-reports klasöründe cucumber.json oluştuğundan emin olun.
                cucumber buildStatus: 'UNSTABLE',
                        fileIncludePattern: '**/cucumber.json',
                        jsonReportDirectory: 'target/json-reports',
                        sortingMethod: 'ALPHABETICAL'
            }
        }
    }

    post {
        always {
            // Test bitince Extent Reports veya diğer tüm çıktıları (target altındaki) Jenkins'e arşivler
            archiveArtifacts artifacts: 'target/**/*.*', fingerprint: true
        }
    }
}