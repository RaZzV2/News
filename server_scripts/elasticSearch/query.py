from elasticsearch import Elasticsearch

es = Elasticsearch(
    ['http://localhost:9200'],
    basic_auth=('elastic', 'Abcabc123!')
)

POST /news_articles/search 
{
    "query": {
        "script_score": {
            "query": {
                "match_all": {}
            },
            "script": {
                "source": "cosineSimilarity(params.query_vector, 'featureVector') + 1.0",
                "params": {
                    "query_vector": [-0.5243917107582092,-0.10174378007650375,-0.3194262385368347,-0.6704179048538208,-0.3254700303077698,-1.1519131660461426,-1.0763226747512817,-0.04358464479446411,0.25322896242141724,0.8349609971046448,0.720740020275116,-0.20861868560314178,-0.4155184030532837,-1.4744961261749268,0.24566850066184998,-0.6303625106811523,-0.4726419150829315,-0.2731432318687439,-0.03750214725732803,0.23996609449386597,-1.0842562913894653,-0.8510483503341675,-0.526628851890564,-0.8754201531410217,-1.0862454175949097,-0.12280970066785812,0.14840947091579437,0.3143404424190521,-0.26645731925964355,-0.4249958395957947,-1.095129132270813,0.0639515370130539,-0.5858423709869385,-0.36740848422050476,-0.5048751831054688,-0.4135909676551819,-1.2228502035140991,-1.2067073583602905,-0.39812585711479187,-0.3011145293712616,-0.5804567933082581,-0.3512457609176636,-0.862610936164856,-0.12671145796775818,-0.1350361853837967,-0.13005006313323975,-0.4054163992404938,-0.35871773958206177,-0.2507341802120209,-0.4150056838989258,-1.1104971170425415,0.99467933177948,-0.056586503982543945,-0.5845159292221069,-1.8373265266418457,-0.12781482934951782,-0.4193897843360901,-0.33283960819244385,-1.0996363162994385,-1.0429154634475708,0.4490535259246826,-0.652641773223877,-0.4833037555217743,-0.5066666007041931,1.5377639532089233,-0.5176454782485962,-0.06137004867196083,-0.6036536693572998,0.6180065870285034,0.1419643610715866,-0.15323157608509064,-0.3229595124721527,-0.5850828289985657,-0.45569753646850586,-0.3147699236869812,-0.4796954393386841,-1.2843925952911377,-0.5177530646324158,-0.7668400406837463,-0.7611310482025146,0.42742812633514404,-0.31860828399658203,-0.8650349974632263,0.909652054309845,0.03316029906272888,0.6724081039428711,-0.7487086057662964,-1.7525633573532104,-0.8164130449295044,-0.0016456283628940582,-1.0940617322921753,-0.5987276434898376,-1.6643413305282593,-0.6978036761283875,-1.826722264289856,-2.070640802383423,-1.4843418598175049,-0.6067182421684265,-0.8315399885177612,-0.8589771389961243,-0.5539472103118896,0.5396824479103088,-1.7173614501953125,-2.256464958190918,-1.3916717767715454,-1.1977314949035645,-1.5036100149154663,-1.332658290863037,0.5233409404754639,-1.108339786529541,-1.132591962814331,0.04014107584953308,-1.3291370868682861,-0.6166885495185852,-0.7755010724067688,-0.42885085940361023,-1.419189214706421,-0.07531437277793884,-1.2407995462417603,-1.2891151905059814,-0.701119601726532,-0.35709938406944275,-0.4380475878715515,-0.5284650325775146,-1.0386645793914795,-0.8344148397445679,-1.1763601303100586,-0.6517372131347656,-1.347847819328308,-0.944460391998291,-0.3708488941192627,-1.3271987438201904,-0.37321603298187256,-0.7978261113166809,-0.061338167637586594,-0.8850651383399963,-0.5466536283493042,-0.11530833691358566,-1.1680433750152588,-0.21489913761615753,-0.5187420845031738,-1.0123602151870728,-0.5667145848274231,-0.9200355410575867,-0.5621763467788696,-0.9368059635162354,0.01604912057518959,-0.34897580742836,-0.42386069893836975,-0.6234791874885559,0.11383754760026932,-2.199434280395508,-0.6116233468055725,-0.9925504326820374,-1.8713065385818481,-1.295363426208496,-0.7178722620010376,-1.7363938093185425,-1.1823548078536987,-1.1992847919464111,-0.3095768392086029,-0.2606084644794464,-1.1034727096557617,0.09503260254859924,-1.2673397064208984,-0.9217095375061035,-0.8166849613189697,-0.7062512636184692,-1.4660046100616455,-0.5698301196098328,-1.527656078338623,-2.2014565467834473,-2.2407126426696777,-1.4969679117202759,-2.198848009109497,-0.2188609391450882,-1.7325878143310547,-0.8774014711380005,-0.3943541646003723,-2.7359607219696045,-1.926923394203186,-0.6669767498970032,-1.4294443130493164,0.2685106694698334,-0.24259281158447266,-0.9600951671600342,-0.6587013602256775,-1.1579240560531616,0.2796483635902405,0.8350189328193665,-0.03833363205194473,0.4616432189941406,-0.8517425656318665,-0.6055577397346497,-1.4806673526763916,-1.12425696849823,-0.8869851231575012,0.30307435989379883,-0.6932821869850159,1.0811145305633545,-1.7919116020202637,0.11958140134811401,-1.2809150218963623,-1.04463529586792,-1.9507601261138916,-1.379976749420166,0.20414382219314575,-0.7324355840682983,-1.2124465703964233,-0.5277306437492371,-1.6802973747253418,-0.3935372829437256,0.1107945367693901,-0.031052546575665474,0.15360431373119354,-1.1008050441741943,-0.8237969875335693,-0.9595912098884583,-0.82319176197052,0.21463754773139954,-0.2174721658229828,-0.5639883875846863,-1.245858907699585,-1.4175828695297241,-0.7487413287162781,-1.0181864500045776,0.011144395917654037,-2.193904161453247,-0.6984683871269226,-1.3991092443466187,-1.1527774333953857,-1.3875490427017212,-1.0409411191940308,0.812350332736969,-0.8501863479614258,-0.649387001991272,-1.8874436616897583,-1.0011425018310547,-0.5284261703491211,-0.42522186040878296,-1.2617785930633545,-2.029906749725342,-1.2913709878921509,-0.35166195034980774,-1.0339189767837524,-1.3208239078521729,-0.5393134355545044,-0.7880772948265076,-0.9660772085189819,-1.279862880706787,-1.2535319328308105,-0.4060263931751251,-0.23563173413276672,-2.2800793647766113,-1.2746520042419434,0.11094673722982407,-0.2999872863292694,-0.6613866090774536,-0.9304876923561096,-1.3862453699111938,-1.3606932163238525,-1.8310166597366333,-0.413394570350647,-1.156335711479187,-1.6143598556518555,-0.780060350894928,-0.24158410727977753,0.21340420842170715,-2.837172031402588,-1.2786377668380737,-0.999742329120636,0.4220927953720093,-0.726757824420929,-1.2326279878616333,-0.7616908550262451,-0.7797693014144897,-1.3802990913391113,-0.6927087306976318,-0.48001015186309814,-1.5674328804016113,-0.8897969722747803,-0.30797481536865234,-0.6843637824058533,-0.8941852450370789,-0.41614365577697754,-0.3285388648509979,-0.20683176815509796,-0.6306466460227966,-1.1427819728851318,-1.4757100343704224,-1.2016828060150146,0.02853051759302616,-0.761383056640625,-1.4356476068496704,-0.003489918075501919,0.5142426490783691,-0.8533618450164795,0.5817375779151917,-1.6622451543807983,-1.458101749420166,-1.3009111881256104,-0.985375702381134,-0.4501503109931946,-0.7587156891822815,-1.0635099411010742,-0.9651779532432556,-0.8871302008628845,-0.7177770137786865,-0.7544251084327698,-0.6349459290504456,-1.4247004985809326,-0.2604198753833771,-1.3186595439910889,-0.9608917832374573,-0.8958139419555664,-0.2888720631599426,-0.882652759552002,-0.5263753533363342,-0.8058114647865295,-1.2432317733764648,-1.782159447669983,-0.007115466520190239,-0.6124573349952698,-0.3864126205444336,-0.8706708550453186,-0.5547947883605957,-0.20840297639369965,-0.4766985774040222,-0.5739816427230835,-1.2250081300735474,-1.64886474609375,-1.6282674074172974,-0.6450198888778687,-0.9029600024223328,-1.8387174606323242,-1.0707274675369263,-0.37780919671058655,-2.1361782550811768,-1.2170876264572144,-0.25218334794044495,-0.4658340513706207,-1.7866953611373901,-1.1720348596572876,-1.2935850620269775,-0.9238643050193787,-0.466741681098938,-0.0719071626663208,0.07741422206163406,-1.0811320543289185,-0.07138553261756897,-0.42796412110328674,0.2511408030986786,-0.9598684906959534,-1.379765510559082,0.8484871983528137,-0.5376902222633362,-2.0975236892700195,-1.7900283336639404,-1.0286786556243896,-1.2528495788574219,-1.7492327690124512,-1.1239932775497437,-1.2409271001815796,-0.7993314266204834,-0.9040643572807312,0.7016381025314331,-0.047923095524311066,-0.8100438714027405,-0.7422443628311157,0.3056006133556366,-1.16144597530365,-0.6557102203369141,-0.7351424694061279,-0.9233471751213074,-0.5667905211448669,-0.2736961245536804,-0.12192638218402863,-1.1819123029708862,-0.20096471905708313,-0.1149136945605278,-1.168436050415039,0.5545465350151062,-0.09417935460805893,-0.824463963508606,-0.526516854763031,0.6652758121490479,0.5679949522018433,0.11018460988998413,0.9749671816825867,-0.7331376075744629,-0.03505530208349228,0.6557648777961731,0.21435421705245972,-0.0679992288351059,-0.5853785872459412,-0.8395677804946899,-0.38727423548698425,-1.9226510524749756,0.07669740915298462,0.003044181503355503,0.22078324854373932,1.4124730825424194,-0.3097687065601349,0.4311613440513611,0.42105498909950256,0.8584210276603699,-0.22737962007522583,3.63919734954834,2.3844385147094727,2.1647729873657227,0.43841060996055603,0.02804243378341198,0.8144881725311279,0.8501496315002441,-0.6413191556930542,1.8329920768737793,0.14773887395858765,0.48688048124313354,0.3606807291507721,0.33413153886795044,0.11554259061813354,1.0856760740280151,0.4199058711528778,-0.4843335747718811,0.5879538655281067,0.7435125708580017,0.2580485939979553,0.6076701283454895,-0.17319831252098083,0.010044141672551632,-0.5653907656669617,0.34395068883895874,-0.2801136374473572,0.04485710710287094,-0.6609960198402405,-0.4274301826953888,6.7079315185546875,0.11502925306558609,-0.7904385924339294,1.6738117933273315,-0.07999426871538162,-0.12732186913490295,0.9465196132659912,0.3206213116645813,0.12982767820358276,0.42391812801361084,0.5835296511650085,0.531684935092926,-0.20898474752902985,0.457233726978302,-0.27960142493247986,-0.2020924985408783,-0.0015318496152758598,-0.4917762279510498,0.3804282546043396,2.0864319801330566,0.07066242396831512,0.5252756476402283,0.35374462604522705,0.5900014638900757,0.6812112927436829,1.680111289024353,0.5702107548713684,-0.4392685294151306,-0.049050696194171906,0.27795088291168213,1.7468070983886719,0.7714504599571228,5.782485008239746,-0.4645562171936035,0.690592885017395,1.0419726371765137,0.02612755447626114,-0.45639657974243164,-0.26283368468284607,2.3925375938415527,0.34233763813972473,0.2351287305355072,0.30704617500305176,5.488605499267578,2.7836432456970215,-0.553648829460144,1.958903431892395,1.0745835304260254,0.07532454282045364,0.43617698550224304,-0.6445501446723938,0.5580421686172485,0.09414884448051453,0.9177359938621521,0.23816558718681335,0.3958315849304199,-1.1221356391906738,-0.030187729746103287,1.164351224899292,-1.3683451414108276,-0.04894386976957321,-0.10460639744997025,3.6521527767181396,0.16169968247413635,0.0187423937022686,0.7066563367843628,0.2929714024066925,-0.24197827279567719,0.01715940795838833,-0.3628702759742737,1.3432741165161133,0.009292494505643845,-0.7700842618942261,1.989842414855957,-0.5304027199745178,5.7380828857421875,-0.14931286871433258,1.2286453247070312,0.982403576374054,-0.04956180602312088,-0.14802347123622894,1.550102710723877,-0.9330469369888306,-0.09464795142412186,0.16959251463413239,-1.9725191593170166,0.024995699524879456,1.3688338994979858,1.0232250690460205,0.29467684030532837,0.014005130156874657,0.5730686187744141,-0.3579227924346924,0.126258984208107,0.6340571641921997,0.016980793327093124,-0.16215097904205322,-0.28503474593162537,-0.6062423586845398,0.5541187524795532,0.7841513156890869,0.1729978322982788,0.5788798332214355,0.33418744802474976,0.2245936393737793,0.5742327570915222,0.6312510967254639,-0.10587533563375473,-1.2537267208099365,0.6621872186660767,-0.01814226061105728,1.0559338331222534,-0.19232523441314697,0.2843087613582611,-0.04566367715597153,-0.08989088237285614,-0.7318131923675537,0.6239569187164307,0.1756681352853775,3.5579562187194824,-0.4235386550426483,2.079503297805786,0.8432585000991821,-0.3125440180301666,-0.5750135779380798,2.632643461227417,0.8076640367507935,0.9225051403045654,-0.07239022105932236,1.9383251667022705,0.570624053478241,-0.003513381816446781,0.9304735660552979,2.5973892211914062,-0.04335454851388931,3.288619041442871,-1.1238850355148315,0.040378570556640625,0.0706074982881546,2.469918727874756,0.0408908873796463,1.020906686782837,-0.03244398534297943,-0.7260611057281494,0.22205957770347595,6.500977993011475,2.786404609680176,0.9902275800704956,0.8792930841445923,-0.12537842988967896,2.6359376907348633,0.03867316246032715,0.11759468168020248,-1.4951696395874023,1.3803459405899048,0.14859893918037415,-0.10630012303590775,-0.5805356502532959,-0.6968939304351807,1.9375759363174438,-0.06822565943002701,-0.6893664002418518,1.1032581329345703,-0.03529517725110054,2.26210880279541,0.027899449691176414,0.67873215675354,-0.11759548634290695,-0.11628307402133942,-0.016660263761878014,-1.0169447660446167,-0.5303596258163452,0.37226733565330505,7.442061424255371,0.7687340378761292,0.04669159650802612,0.19271615147590637,1.8974344730377197,0.44662460684776306,-1.1513184309005737,0.3513156771659851,0.5309842824935913,-0.6482190489768982,0.17460627853870392,0.6018957495689392,2.038175106048584,-0.22620165348052979,0.06509950757026672,0.7489925026893616,-0.5357258319854736,0.10898663103580475,7.780397415161133,-0.5252233147621155,-0.6918902397155762,0.5062150955200195,0.050097882747650146,1.3906337022781372,-0.9545960426330566,1.8599200248718262,0.24141229689121246,0.31777480244636536,0.5203754305839539,1.7588143348693848,0.026592900976538658,0.7532416582107544,-0.14205071330070496,-0.32147008180618286,0.03742336481809616,0.05271052196621895,0.42381492257118225,0.6862276792526245,-1.0961475372314453,-0.07070917636156082,0.58656245470047,-0.28442880511283875,-1.0007827281951904,0.5945293307304382,0.17555294930934906,4.0876970291137695,0.8938986659049988,9.183027267456055,0.006364616099745035,0.046628437936306,-1.0457769632339478,1.9079183340072632,1.8917111158370972,0.3470751643180847,0.4484439492225647,0.37606945633888245,1.0476423501968384,0.09088662266731262,-0.16067665815353394,0.4398338198661804,-0.6755205988883972,1.4600396156311035,0.6611596941947937,1.9283537864685059,0.14040584862232208,-0.916958749294281,3.345149040222168,-1.6009024381637573,1.0705626010894775,0.7361400723457336,-0.3096797466278076,0.24904002249240875,0.3243100941181183,1.0704584121704102,1.1699540615081787,-0.9320717453956604,0.05877198278903961,0.6043885350227356,-0.5417719483375549,0.08697802573442459,0.13601605594158173,0.22545833885669708,0.1434098184108734,0.34145739674568176,0.8069644570350647,0.15384410321712494,0.09442446380853653,-0.02962678298354149,0.7046789526939392,1.6884467601776123,-0.11525445431470871,0.9329004883766174,0.44050413370132446,0.9593221545219421,0.5859664082527161,2.8755691051483154,3.289809465408325,0.6035674810409546,1.218184471130371,1.7381694316864014,-0.5391115546226501,0.699171245098114,0.07091052085161209,-0.5500365495681763,0.9903450012207031,0.4088784456253052,1.1661179065704346,0.49819427728652954,6.14243221282959,1.6284502744674683,-1.529203176498413,0.5521247386932373,-0.05641338974237442,0.6158921122550964,-0.16145017743110657,0.819949746131897,-1.3077096939086914,0.10404946655035019,1.265784502029419,0.6703710556030273,-0.007220398634672165,1.6064026355743408,1.54945707321167,-1.231805443763733,2.8119070529937744,5.150407314300537,0.24835105240345,-0.2674446702003479,0.7226913571357727,-0.2575608491897583,-0.6055595874786377,0.22949692606925964,-1.016878604888916,1.6151127815246582,0.23710176348686218,1.7597044706344604,0.4608056843280792,1.2793346643447876,0.4356006681919098,0.06292510032653809,-0.2555359899997711,0.8138566017150879,2.9198405742645264,-0.07266182452440262,0.7502833008766174,1.6428163051605225,0.1507968008518219,0.4379896819591522,2.9275283813476562,0.4497998058795929,-0.3295952379703522,0.9632348418235779,0.1867898404598236,2.07513165473938,-0.37743866443634033,0.3601857125759125,0.29046058654785156,-0.7105058431625366,1.0501797199249268,-0.46846675872802734,0.14814218878746033,-0.3206509053707123,-0.7594492435455322,-0.5167149305343628,-0.047678619623184204,0.07984147220849991,-1.4241278171539307,-0.17290887236595154,-0.30041423439979553,0.3366564214229584,1.9413210153579712,0.21722978353500366,1.5427526235580444,1.373924732208252,1.0181301832199097,-0.37038907408714294,0.8358098268508911,0.723691463470459,1.8755989074707031,1.6821082830429077,0.0016562910750508308,0.663036584854126,1.3895576000213623,0.15710216760635376,-0.7753645777702332,0.4432302713394165,1.2323263883590698,-0.2959745526313782,0.7937876582145691,-0.251930296421051,2.1779305934906006,0.2912828326225281,-0.7211905121803284,0.6242206692695618,4.190369129180908,0.3315284252166748,-1.2043178081512451,-0.6783882975578308,1.5267950296401978,-0.21850088238716125,-1.2320278882980347,1.2203342914581299,0.20958653092384338,1.018515706062317,-1.1071110963821411,0.36934444308280945,-0.49453508853912354,-0.4381702244281769,4.965619087219238,1.7484314441680908,1.7235339879989624,0.5509220957756042,0.721367359161377,-0.09171634167432785,-0.37745800614356995,-0.219132661819458,1.141452670097351,0.23997369408607483,0.7334361672401428,-0.6048904061317444,0.8273186683654785,0.9298808574676514,-0.0023297611624002457,0.15164892375469208,-0.07530215382575989,0.030005019158124924,1.5803165435791016,1.051248550415039,0.3461487889289856,0.23838472366333008,0.9912407398223877,0.5356706976890564,0.44365978240966797,0.607060968875885,0.1425076276063919,0.7837049961090088,-0.7530369162559509,-0.2524958848953247,1.143800139427185,0.5562014579772949,-1.5180070400238037,0.8310773372650146,1.0141716003417969,-1.4682838916778564,1.0611097812652588,1.153376579284668,0.7385047674179077,1.9158446788787842,0.6199954748153687,0.32815131545066833,-0.45133572816848755,2.6139559745788574,0.3005109429359436,1.0583217144012451,1.629671573638916,4.7441606521606445,2.6758110523223877,2.8593688011169434,2.5791096687316895,-0.5993688702583313,1.3138395547866821,-0.13755899667739868,0.23553211987018585,0.7489818930625916,0.18450844287872314,1.213181972503662,0.660079836845398,-0.532554030418396,1.1530729532241821,0.09397672116756439,1.332733392715454,1.8986793756484985,-1.0813491344451904,1.0879777669906616,-0.7888063192367554,-0.46223288774490356,-0.014878761023283005,1.9052083492279053,0.12920285761356354,0.6199433207511902,-1.1735869646072388,-0.9224986433982849,-0.45314526557922363,0.7399588823318481,-0.5082736611366272,0.8378318548202515,0.49798160791397095,0.002448420971632004,1.2269362211227417,0.09429576992988586,-0.8305685520172119,0.7613316178321838,-0.5449094772338867,-0.14634397625923157,1.1184993982315063,0.397234708070755,1.1213828325271606,1.0096690654754639,-0.5439150929450989,0.1909119337797165,-0.33545053005218506,-1.1990230083465576,0.30989521741867065,1.371424674987793,0.4867439866065979,-0.6152361035346985,1.0106617212295532,1.2442036867141724,1.3909804821014404,0.686656653881073,2.6145179271698,2.444298028945923,0.5369260311126709,0.28514009714126587,-0.6821378469467163,-0.8545855283737183,-0.9663296937942505,-0.6613120436668396,-0.984529435634613,-1.2516473531723022,-1.3581801652908325,-0.048961758613586426,-0.3876064121723175,0.3953154385089874,-0.2081417292356491,0.26310616731643677,-0.8268451690673828,-0.8996939659118652,-1.2033518552780151,-0.619763970375061,-0.7258228659629822,-0.3443710505962372,-0.97705078125,-0.7807224988937378,-0.3231191337108612,-0.6598117351531982,0.40501299500465393,-0.5910414457321167,-0.16667407751083374,-0.32564282417297363,0.07545627653598785,0.7111401557922363,0.3149874210357666,-0.3071104884147644,-0.38692179322242737,-0.6205278635025024,-0.4311631917953491,-0.6938666105270386,-0.4481484889984131,-0.04941645637154579,-0.3057624399662018,-0.5443893074989319,-0.3607461452484131,-0.8872995376586914,-0.9639993906021118,-1.2517929077148438,-1.4572619199752808,-1.559468150138855,-0.03437299653887749,-0.626634418964386,-0.3232946991920471,0.06774148344993591,-0.41348904371261597,-0.22583679854869843,-0.45359107851982117,-0.7398017644882202,0.4545198082923889,0.4596516788005829,0.34515148401260376,0.4279695749282837,0.9898694753646851,0.19219006597995758,0.5797931551933289,1.3023494482040405,-0.6636829972267151,0.0383022278547287,0.22531229257583618,-0.3510732054710388,-1.2160522937774658,-0.37954944372177124,-0.023372454568743706,0.7729783654212952,0.3339056074619293,-0.2631019651889801,-0.0962977483868599,-0.9013655185699463,0.48812437057495117,-0.4478801190853119,-0.06545303761959076,-0.8484593629837036,0.4003058671951294]
		}
            }
        }
    }
}

res = es.search(index='news_articles', body=query)
print(res)
