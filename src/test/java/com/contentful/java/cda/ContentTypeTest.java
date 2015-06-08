package com.contentful.java.cda;

import com.contentful.java.cda.lib.Enqueue;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ContentTypeTest extends BaseTest {
  @Test
  @Enqueue("content_types_cat.json")
  public void fetchContentType() throws Exception {
    CDAContentType cat = client.observe(CDAContentType.class).one("cat").toBlocking().first();
    assertThat(cat.name()).isEqualTo("Cat");
    assertThat(cat.displayField()).isEqualTo("name");
    assertThat(cat.description()).isEqualTo("Meow.");
    assertThat(cat.fields()).hasSize(8);
  }

  @Test
  @Enqueue({ "content_types_cat.json", "content_types_fake.json" })
  public void manuallyFetchedContentTypeIsCached() throws Exception {
    client.observe(CDAContentType.class).one("cat").toBlocking().first();
    assertThat(client.cache.types()).hasSize(5);
    assertThat(client.cache.types()).doesNotContainKey("fake");

    CDAContentType fake = client.observe(CDAContentType.class).one("fake").toBlocking().first();
    assertThat(client.cache.types()).hasSize(6);
    assertThat(client.cache.types().get(fake.id())).isSameAs(fake);
  }
}