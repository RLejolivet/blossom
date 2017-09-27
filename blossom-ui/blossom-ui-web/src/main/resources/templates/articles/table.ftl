<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>

<@table.pagetable
  page=articles
  label='articles.label'
  iconPath='fa fa-pencil'
  columns= {
  "name": { "label":"articles.article.properties.name", "sortable":true, "link":"/blossom/content/articles/{id}"},
  "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
  filters=[]
  searchable=true
  q=q
/>
