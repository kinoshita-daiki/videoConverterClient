[![Deploy Status](https://github.com/kinoshita-daiki/videoConverterClient/actions/workflows/deploy.yml/badge.svg)](https://github.com/kinoshita-daiki/videoConverterClient/actions?query=workflow%3ADeploy)

[![CI Status](https://github.com/kinoshita-daiki/videoConverterClient/actions/workflows/buildAndTest.yml/badge.svg)](https://github.com/kinoshita-daiki/videoConverterClient/actions?query=workflow%3AMyPipeline)

ポートフォリオとして作成した、動画変換サイトのクライアントサーバ側のリポジトリです。<br>
バックエンドサーバ側は[こちらです](https://github.com/kinoshita-daiki/videoConverter)

- [動画変換サイト](#動画変換サイト)
- [動画変換の仕方](#動画変換の仕方)
- [特徴](#特徴)
- [使用技術](#使用技術)

# 動画変換サイト
下記よりWebアプリをご利用ください。<br>
https://kinoshitadaiki.work/vc/videoEncoderInput

> [!NOTE]
> このWebアプリはポートフォリオとして作成されています。機能は簡素であることにご留意ください。

# 動画変換の仕方
1. 「ファイルを選択」から変換したい動画ファイルを選択します。
	- (Option)「試用変換元動画DL」からテスト用に使える動画をDLできます。
1. 切り抜き時間を入力します。
1. メールアドレスを入力します。変換後、DLリンクの記載されたメールが入力されたアドレスに届きます。
1. 変換ボタンを押下します。
1. 変換開始を示す画面が表示されるので、しばらく待ちます。
1. 指定したメールアドレスに、変換完了メールが届きます。
1. メール内の指示に従い、ダウンロードページに飛びます。
1. ページ内のダウンロードリンクより、変換された動画のダウンロードができます。
	- ダウンロード保障期日から1時間ほど経ちますとダウンロードできなくなります。ご注意ください。

# 特徴
Apache MQ ArtemisとJMSを利用しています。これにより変換リクエストをキューイングして[変換サーバ](https://github.com/kinoshita-daiki/videoConverter)に非同期かつ適切なタイミングでの変換を可能にしています。<br>
プッシュされた場合、自動でコンパイル、テスト、テストレポートを出力するパイプラインを構築しています。<br>
また手動トリガーでパッケージの作成(RunnableJar)、サーバへの自動デプロイが可能です。

# 使用技術
- Java(17)
- Spring Boot
	- Spring MVC
		- Thymeleaf
	- JMS
	- Rest
- Docker
- Maven
- lombok

その他については[pom.xml](https://github.com/kinoshita-daiki/videoConverterClient/blob/master/pom.xml)を参照してください。