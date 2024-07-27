package ly.david.musicsearch.shared.feature.graph

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewArtistCollaborationGraphUi() {
    PreviewTheme {
        Surface {
            ArtistCollaborationGraphUi(
                links = listOf(
                    GraphLink(
                        x2 = 50.0,
                        y2 = 50.0
                    ),
                    GraphLink(
                        x1 = 50.0,
                        y1 = 50.0,
                        x2 = 100.0,
                        y2 = 100.0,
                    ),
                ),
                nodes = listOf(
                    GraphNode(id="6114677c-fa8f-4d87-960a-3f1169aaef89",name="TK from 凛として時雨",entity=MusicBrainzEntity.ARTIST,radius=13.0,x=21.52809243865624,y=-32.282044190199315), GraphNode(id="dfc6a151-3792-4695-8fda-f64723eaa788",name="ヨルシカ",entity=MusicBrainzEntity.ARTIST,radius=16.0,x=-22.53831004689229,y=49.79169372410625), GraphNode(id="80b3cb83-b7a3-4f79-ad42-8325cefb3626",name="キタニタツヤ",entity=MusicBrainzEntity.ARTIST,radius=12.0,x=-63.364969619416186,y=-28.15655738273208), GraphNode(id="b3933fd6-fba9-44b1-992a-1e8128324ca1",name="下村陽子",entity=MusicBrainzEntity.ARTIST,radius=12.0,x=64.98845223719859,y=40.75291587623227), GraphNode(id="cb191900-8ad8-46b9-b021-a093ee2b2f9b",name="SawanoHiroyuki[nZk]",entity=MusicBrainzEntity.ARTIST,radius=11.0,x=102.85320623407607,y=-77.92905566373817), GraphNode(id="16a3c7da-2951-4020-b1e3-4a10cecb7141",name="Lanndo",entity=MusicBrainzEntity.ARTIST,radius=12.0,x=69.14838757319082,y=124.6492237710781), GraphNode(id="66bdd1c9-d1c5-40b7-a487-5061fffbd87d",name="Eve",entity=MusicBrainzEntity.ARTIST,radius=13.0,x=-14.7126195014333,y=138.44629187073772), GraphNode(id="cb3e092f-5eae-4bc1-9ffe-bbdc13dcd823",name="MONDO GROSSO",entity=MusicBrainzEntity.ARTIST,radius=11.0,x=-118.10155714257671,y=34.21914073371545), GraphNode(id="26b8ea1c-fb9e-4378-84a0-d0eace285f7e",name="SennaRin",entity=MusicBrainzEntity.ARTIST,radius=11.0,x=135.6540727783299,y=-2.78003049420636), GraphNode(id="494ac693-6d6a-4411-ac41-d6e7c32c6e34",name="melt",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=-199.08007314059407,y=83.89741248418153), GraphNode(id="51999312-1044-4f91-92b0-ed23974929ca",name="melt",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=119.5813650985227,y=-263.14798520958806), GraphNode(id="e41f1853-0f2a-4ec8-a815-7af70087bb85",name="melt",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=48.285419711054,y=215.57847410406984), GraphNode(id="58576a03-f302-4f68-9a97-113a6afc753f",name="星めぐる詩",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=-137.9705023431522,y=-172.35174123884116), GraphNode(id="9a70f454-1da3-4570-b592-33e5f8713d28",name="ナイトルーティーン",entity=MusicBrainzEntity.RECORDING,radius=12.0,x=185.28077781329563,y=-87.63827083836911), GraphNode(id="d4a15774-05b7-4f58-97b1-2bdf4a10d8d5",name="ナイトルーティーン",entity=MusicBrainzEntity.RECORDING,radius=12.0,x=-243.50670445389656,y=154.35233794997583), GraphNode(id="028c2091-8371-42e3-acf3-82164552691e",name="#時をめくる指 (instrumental)",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=-34.80945903540645,y=-192.33662393616737), GraphNode(id="331c0c61-b16f-406b-b485-15f50225790b",name="サンサーラ",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=227.71520037815085,y=53.89982230502901), GraphNode(id="494377fd-2367-46a1-acf3-7661e8dee00d",name="B∀LK",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=-146.45317226545873,y=-54.57512838546495), GraphNode(id="4c79ceea-5177-4fb7-b7c3-0a1e6785ee23",name="宇宙の季節",entity=MusicBrainzEntity.RECORDING,radius=12.0,x=29.573958372146755,y=-116.88746636488253), GraphNode(id="546560d5-dc85-45aa-9627-9a482c09471e",name="#時をめくる指",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=-90.6185383042285,y=235.28039464644218), GraphNode(id="a5c71240-a1d4-4884-9663-2aba6d9d7165",name="宇宙の季節",entity=MusicBrainzEntity.RECORDING,radius=12.0,x=-54.2584394789656,y=-111.64856664587037), GraphNode(id="b2ede43d-ed7d-4d40-99bb-dd8c4fed9ded",name="最後の心臓",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=138.56709658550494,y=79.16679693327873), GraphNode(id="b80166fd-f19e-4bdf-82b9-22929f6775ca",name="vous",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=-94.69545301691291,y=112.80339088121619), GraphNode(id="bd3cbe82-eb42-4e46-958a-522a7b22f750",name="少年時代 (あの夏のルカver.)",entity=MusicBrainzEntity.RECORDING,radius=11.0,x=46.99264927811805,y=-198.03321035005197)
                ),
            )
        }
    }
}
